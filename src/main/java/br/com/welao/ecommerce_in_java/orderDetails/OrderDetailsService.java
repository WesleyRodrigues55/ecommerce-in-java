package br.com.welao.ecommerce_in_java.orderDetails;

import br.com.welao.ecommerce_in_java.addresses.Addresses;
import br.com.welao.ecommerce_in_java.addresses.AddressesRepository;
import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.carts.CartRepository;
import br.com.welao.ecommerce_in_java.payments.Payments;
import br.com.welao.ecommerce_in_java.payments.PaymentsRepository;
import br.com.welao.ecommerce_in_java.user.User;
import br.com.welao.ecommerce_in_java.user.UserRepository;
import br.com.welao.ecommerce_in_java.utils.Utils;
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

        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.setOrderStatus("CLOSED");
        Utils.copyNonNullProperties(orderDetailsDTO, orderDetails);
        this.orderDetailsRepository.save(orderDetails);

        return orderDetails.getCart().getId();
    }
}
