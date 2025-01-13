package br.com.welao.ecommerce_in_java.payments;

import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetailsRepository;
import br.com.welao.ecommerce_in_java.user.User;
import br.com.welao.ecommerce_in_java.user.UserRepository;
import br.com.welao.ecommerce_in_java.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public ResponseEntity<?> create(PaymentsDTO paymentsDTO) {
        long userId = paymentsDTO.getUser().getId();
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // valid information of cart and your payment method

        Payments payments = PaymentsMapper.toEntity(paymentsDTO);
        this.paymentsRepository.save(payments);


        return ResponseEntity.status(HttpStatus.CREATED).body(payments);
    }
}
