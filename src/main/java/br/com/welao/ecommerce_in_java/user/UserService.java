package br.com.welao.ecommerce_in_java.user;

import br.com.welao.ecommerce_in_java.Utils.Utils;
import br.com.welao.ecommerce_in_java.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

}
