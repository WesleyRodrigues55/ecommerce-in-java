package br.com.welao.ecommerce_in_java.user;

import br.com.welao.ecommerce_in_java.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void messageCreateAccount(String to) {

        emailService.sendEmail(
                to,
                "Created Account Successfully",
                "Please use the code '9dwad9a' to login to the www.meusite.com platform.");
    }

    public ResponseEntity<?> create(UserDTO userDTO) {
        var email = userRepository.findByEmail(userDTO.getEmail());
        if (email != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        var phone = userRepository.findByPhone(userDTO.getPhone());
        if (phone != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone already exists");
        }

        var personType = userDTO.getTypePerson();
        switch (personType) {
            case "FÍSICA":
                if (userRepository.findByCpf(userDTO.getCpf()) != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF already exists");
                }
                break;
            case "JURÍDICA":
                if (userRepository.findByCnpj(userDTO.getCnpj()) != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CNPJ already exists");
                }
                break;
        }

        User user = UserMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);

        messageCreateAccount(userDTO.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


}
