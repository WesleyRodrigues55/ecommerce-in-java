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

    @Autowired
    private EmailService emailService;

    public void messageCreateAccount(String to, String verificationCode) {
        String subject = "Created Account Successfully";
        String body = "Please use the code " + verificationCode + " to login to the www.meusite.com/login platform.";

        emailService.sendEmail(to, subject, body);
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

        var code = Utils.generatorRandomCode();
        userDTO.setVerificationCode(code);

        User user = UserMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);

//        messageCreateAccount(userDTO.getEmail(), code);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    public void messageGenerateCodeForLogin(String to, String verificationCode) {
        String subject = "Your code for access is...";
        String body = "Please use the code " + verificationCode + " to login to the www.meusite.com/login platform.";

        emailService.sendEmail(to, subject, body);
    }


    // 1o step - for login
    // get email and generate the code for access
    public ResponseEntity<?> login(UserDTO userDTO) {
        // get input email
        var user = userRepository.findByEmail(userDTO.getEmail());
        if (user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }

        var code = Utils.generatorRandomCode();
        user.setVerificationCode(code);

        // update column verificationCode
        Utils.copyNonNullProperties(userDTO, user);
        userRepository.save(user);

        // send email with code access
//        messageGenerateCodeForLogin(userDTO.getEmail(), code);

        // OK
        return ResponseEntity.status(HttpStatus.OK).body("Code generated successfully!");
    }

    // 2o step - for login
    // get email and generated code for create auth and successful login
    public ResponseEntity<?> validateCodeForAccessAndCreateAuth(UserDTO userDTO) {
        // validate email
        var user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }

        // validate code
        var code = user.getVerificationCode();
        if (code == null || !code.equals(userDTO.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect verification code");
        }

        // generate auth and expires session

        // OK
        return ResponseEntity.status(HttpStatus.OK).body("Login successful!");
    }



}
