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
import java.util.stream.Collectors;

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
            this.paymentsService.createPaymentIntent(paymentId, amount, currency);

            long cartIdInOrderDetails = updatedOrderStatus(orderDetailsId);
            this.cartService.updatedStatusCart(cartIdInOrderDetails, amount);

            var emailUser = this.userService.getEmailUserById(userId);
            this.messagePurchaseSuccessfully(emailUser, orderDetails);

            return ResponseEntity.status(HttpStatus.CREATED).body("OrderDetails has been completed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public void messagePurchaseSuccessfully(String to,OrderDetails orderDetails) {
        String subject = "Your purchase has finished successfully!";

        String body = generateOrderDetailsHeadHtml() +
                "<body style=\"text-align:center; max-width: 600px; margin: 0 auto;\">" +
                    "<h1>Order Confirmation #" + orderDetails.getId() + "</h1>" +
                    "<p>Hello,</p>" +
                    "<p>Thank you for your purchase! Here are the details your order:</p>" +
                    "<hr>" +
                    generateOrderDetailsTableHtml(orderDetails) +
                    "<hr>" +
                    generateShippingAddressTableHtml(orderDetails) +
                    "<hr>" +
                    generateOrderItemsTableHtml(orderDetails) +
                    "<hr>" +
                    "<p>If you have any questions or need support, please contact us.</p>" +
                    "<p><strong>Best regards,</strong><br>EcommerceInJava</p>" +
                "</body>" +
            "</html>";

        this.emailService.sendHtmlEmail(to, subject, body);
    }

    private String generateOrderDetailsHeadHtml() {
        return "<!DOCTYPE html>" +
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
                "</head>";
    }

    private String generateOrderDetailsTableHtml(OrderDetails orderDetails) {
        return "<table>" +
                    "<thead>" +
                        "<tr>" +
                            "<th colspan=\"2\">Order Details</th>" +
                        "</tr>" +
                    "</thead>" +
                    "<tbody>" +
                        "<tr>" +
                            "<th>Order ID:</th>" +
                            "<td>"+ orderDetails.getId() +"</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<th>Order Data:</th>" +
                            "<td>"+ orderDetails.getOrderDate() +"</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<th>Order Status:</th>" +
                            "<td>"+ orderDetails.getOrderStatus() +"</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<th>Payment Method:</th>" +
                            "<td>"+ orderDetails.getPayments().getPaymentMethodType() +"</td>" +
                        "</tr>" +
                    "</tbody>" +
                "</table>";
    }

    private String generateOrderItemsTableHtml(OrderDetails orderDetails) {
        return "<table>" +
                "<thead>" +
                    "<tr>" +
                        "<th colspan=\"4\">Order Items</th>" +
                    "</tr>" +
                    "<tr>" +
                        "<th>Product</th>" +
                        "<th>Quantity</th>" +
                        "<th>Unit Price</th>" +
                        "<th>Total</th>" +
                    "</tr>" +
                "</thead>" +
                "<tbody>" +
                    orderDetails.getCart().getItemsCart().stream()
                        .map(item ->
                            "<tr>" +
                                "<td>"+ item.getProducts().getName() +"</td>" +
                                "<td>"+ item.getQuantity() +"</td>" +
                                "<td>R$ "+ item.getPrice() +"</td>" +
                                "<td>R$ "+ item.getPrice() * item.getQuantity() +"</td>" +
                            "</tr>"
                        )
                        .collect(Collectors.joining()) +
                    "<tr>" +
                        "<th colspan=\"4\">Total: R$"+ orderDetails.getTotalValue() +"</th>" +
                    "</tr>" +
                "</tbody>" +
            "</table>";
    }

    private String generateShippingAddressTableHtml(OrderDetails orderDetails) {
        return "<table>" +
                    "<thead>" +
                        "<tr>" +
                            "<th colspan=\"2\">Shipping address</th>" +
                        "</tr>" +
                    "</thead>" +
                    "<tbody>" +
                        "<tr>" +
                            "<th>Street:</th>" +
                            "<td>"+ orderDetails.getAddresses().getStreet() +"</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<th>Complement:</th>" +
                            "<td>"+ orderDetails.getAddresses().getComplement() +"</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<th>City/State:</th>" +
                            "<td>"+ orderDetails.getAddresses().getCity() +" / "+ orderDetails.getAddresses().getState() +"</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<th>Zip Code:</th>" +
                            "<td>"+ orderDetails.getAddresses().getPostalCode() +"</td>" +
                        "</tr>" +
                    "</tbody>" +
            "</table>";
    }
}
