package br.com.welao.ecommerce_in_java.user;

import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email) == null;
    }

    public boolean isPhoneAvailable(String phone) {
        return userRepository.findByPhone(phone) == null;
    }

    public boolean isCpfOrCnpjAvailable(UserDTO userDTO) {
        if ("FÍSICA".equals(userDTO.getTypePerson())) {
            return userRepository.findByCpf(userDTO.getCpf()) == null;
        } else if ("JURÍDICA".equals(userDTO.getTypePerson())) {
            return userRepository.findByCnpj(userDTO.getCnpj()) == null;
        }
        return true;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public String getEmailUserById(long userId) {
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = existingUser.get();
        return user.getEmail();
    }

    public ResponseEntity<?> listPurchasesByUserId(long userId) {
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }


        /*
            STEPS:
                - select orderDetails by user ID
                - build response
         */

        List<OrderDetails> orderDetails = existingUser.get().getOrderDetails();


        return ResponseEntity.status(HttpStatus.OK).body(orderDetails);
    }


}
