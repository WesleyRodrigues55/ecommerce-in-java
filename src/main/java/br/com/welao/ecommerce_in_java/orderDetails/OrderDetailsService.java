package br.com.welao.ecommerce_in_java.orderDetails;

import br.com.welao.ecommerce_in_java.addresses.Addresses;
import br.com.welao.ecommerce_in_java.addresses.AddressesRepository;
import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.carts.CartRepository;
import br.com.welao.ecommerce_in_java.carts.CartService;
import br.com.welao.ecommerce_in_java.email.EmailService;
import br.com.welao.ecommerce_in_java.payments.Payments;
import br.com.welao.ecommerce_in_java.payments.PaymentsDTO;
import br.com.welao.ecommerce_in_java.payments.PaymentsRepository;
import br.com.welao.ecommerce_in_java.payments.PaymentsService;
import br.com.welao.ecommerce_in_java.user.User;
import br.com.welao.ecommerce_in_java.user.UserRepository;
import br.com.welao.ecommerce_in_java.user.UserService;
import br.com.welao.ecommerce_in_java.utils.Utils;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<?> create(OrderDetailsDTO orderDetailsDTO) {
        long cartId = orderDetailsDTO.getCart().getId();
        long userId = orderDetailsDTO.getUser().getId();
        long addressesId = orderDetailsDTO.getAddresses().getId();
        long paymentsId = orderDetailsDTO.getPayments().getId();

        Optional<OrderDetails> existingOrderDetails = this.orderDetailsRepository.findByCartId(cartId);
        if (existingOrderDetails.isPresent()) {
            // att status? or ignore and return error?
            return ResponseEntity.status(HttpStatus.CONFLICT).body("OrderDetails already exists");
        }

        Optional<Cart> existingCart = this.cartRepository.findById(cartId);
        if (existingCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cart not found");
        }
        Cart cart = existingCart.get();

        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }

        Optional<Addresses> existingAddress = this.addressesRepository.findById(addressesId);
        if (existingAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("address not found");
        }

        Optional<Payments> existingPayment = this.paymentsRepository.findById(paymentsId);
        if (existingPayment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("payment not found");
        }

        orderDetailsDTO.setTotalValue(cart.getTotalValue());
        orderDetailsDTO.setOrderStatus("PENDING"); // create enum !!!!

        OrderDetails newOrderDetails = OrderDetailsMapper.toEntity(orderDetailsDTO);

        // Unable to re-run after the first time
        try {
            this.orderDetailsRepository.save(newOrderDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orderDetails", newOrderDetails);
        response.put("userId", userId);
        response.put("cartId", cartId);
        response.put("addressId", addressesId);
        response.put("paymentId", paymentsId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<?> listByOrderDetailsId(long orderDetailsId) {
        Optional<OrderDetails> existingOrderDetails = this.orderDetailsRepository.findById(orderDetailsId);
        if (existingOrderDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OrderDetails not found");
        }

        OrderDetails orderDetails = existingOrderDetails.get();

        Map<String, Object> response = new HashMap<>();
        response.put("orderDetails", orderDetails);
        response.put("userId", orderDetails.getUser().getId());
        response.put("cart", orderDetails.getCart());
        response.put("payment", orderDetails.getPayments().getPaymentMethodType());
        response.put("address", orderDetails.getAddresses());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public long updatedOrderStatus(long orderDetailsId) {
        Optional<OrderDetails> existingOrderDetails = this.orderDetailsRepository.findById(orderDetailsId);
        if (existingOrderDetails.isEmpty()) {
            // send email to the user talking about the purchase made (send: review purchase and status payment)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetails not found");
        }

        OrderDetails orderDetails = existingOrderDetails.get();
        orderDetails.setOrderStatus("CLOSED");
        this.orderDetailsRepository.save(orderDetails);

        return orderDetails.getCart().getId();
    }

    public ResponseEntity<?> closeOrderDetailsAndFinishedPurchase(
            long orderDetailsId,
            String currency
    ) throws StripeException {
        Optional<OrderDetails> existingOrderDetails = this.orderDetailsRepository.findById(orderDetailsId);
        if (existingOrderDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OrderDetails not found");
        }
        OrderDetails orderDetails = existingOrderDetails.get();

        var amount = (long) orderDetails.getTotalValue();
        var paymentId = orderDetails.getPayments().getId();
        var userId = orderDetails.getUser().getId();

        try {
            var emailUser = this.userService.getEmailUserById(userId);

            // process payment intent
            this.paymentsService.createPaymentIntent(paymentId, amount, currency);

            // update status purchase in: cart, orderDetails and payment
            long cartIdInOrderDetails = updatedOrderStatus(orderDetailsId);
            this.cartService.updatedStatusCart(cartIdInOrderDetails, amount);

            // send email for user: talking about purchase, payment approved and review buy
            this.messagePurchaseSuccessfully(emailUser, orderDetails);

            // return response
            return ResponseEntity.status(HttpStatus.CREATED).body("OrderDetails updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public void messagePurchaseSuccessfully(String to,OrderDetails orderDetails) {
        String subject = "Your purchase has finished successfully!";

        String body = "<!DOCTYPE html>" +
            "<html lang=\"pt-BR\">" +
                "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>Confirmação do Pedido</title>" +
                    "<style>" +
                        "table {" +
                            "width: 100%;" +
                            "border-collapse: collapse;" +
                        "}" +
                        "th, td {" +
                            "border: 1px solid #ddd;" +
                            "padding: 8px;" +
                        "}" +
                        "th {" +
                            "background-color: #f2f2f2;" +
                        "}" +
                    "</style>" +
                "</head>" +
                "<body style=\"text-align:center; max-width: 600px; margin: 0 auto;\">" +
                    "<h1>Order Confirmation #" + 1 + "</h1>" +
                    "<p>Hello,</p>" +
                    "<p>Thank you for your purchase! Here are the details your order:</p>" +
                    "<hr>" +
                    "<h2>Order Details:</h2>" +
                    "<p><strong>Order ID:</strong> 4</p>" +
                    "<p><strong>Order Data:</strong> 19/01/2025 às 16:01:42</p>" +
                    "<p><strong>Order Status:</strong> Fechado</p>" +
                    "<p><strong>Payment Method:</strong> Cartão</p>" +
                    "<hr>" +
                    "<h2>Shipping address:</h2>" +
                    "<p><strong>Street:</strong> Rua A, nº 124</p>" +
                    "<p><strong>Complement:</strong> 123</p>" +
                    "<p><strong>City/State:</strong> City A / A</p>" +
                    "<p><strong>Zip Code:</strong> 123</p>" +
                    "<hr>" +
                    "<h2>Order Items</h2>" +
                    "<table>" +
                        "<thead>" +
                            "<tr>" +
                                "<th>Product</th>" +
                                "<th>Quantity</th>" +
                                "<th>Unit Price</th>" +
                                "<th>Total</th>" +
                            "</tr>" +
                        "</thead>" +
                        "<tbody>" +
                            "<tr>" +
                                "<td>Product B</td>" +
                                "<td>20</td>" +
                                "<td>R$ 10,00</td>" +
                                "<td>R$ 200,00</td>" +
                            "</tr>" +
                        "</tbody>" +
                    "</table>" +
                    "<p><strong>Order Total:</strong> R$ 200,00</p>" +
                    "<hr>" +
                    "<p>If you have any questions or need support, please contact us.</p>" +
                    "<p><strong>Best regards,</strong><br>EcommerceInJava</p>" +
                "</body>" +
            "</html>";

        this.emailService.sendHtmlEmail(to, subject, body);
    }

}
