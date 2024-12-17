package br.com.welao.ecommerce_in_java.auth;

import br.com.welao.ecommerce_in_java.utils.Utils;
import br.com.welao.ecommerce_in_java.email.EmailService;
import br.com.welao.ecommerce_in_java.infra.security.TokenService;
import br.com.welao.ecommerce_in_java.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<?> register(UserDTO userDTO) {
        if (!this.userService.isEmailAvailable(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        if (!this.userService.isPhoneAvailable(userDTO.getPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone already exists");
        }

        if (!this.userService.isCpfOrCnpjAvailable(userDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF/CNPJ already exists");
        }

        User user = UserMapper.toEntity(userDTO);
        this.userService.saveUser(user);

        this.messageRegister(userDTO.getEmail());

        return ResponseEntity.ok().build();
    }

    public void messageRegister(String to) {
        String subject = "Created Account Successfully";
        String body = "Please go to www.meusite.com/login to log in";

        emailService.sendEmail(to, subject, body);
    }

    public ResponseEntity<?> messageGenerateCodeForLogin(UserDTO userDTO) {
        var user = userService.findByEmail(userDTO.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found");
        }

        var code = Utils.generatorRandomCode();
        String encryptedCodeVerification = new BCryptPasswordEncoder().encode(code);
        user.setVerificationCode(encryptedCodeVerification);

        Utils.copyNonNullProperties(userDTO, user);
        this.userService.updateUser(user);

        generateCodeForLogin(userDTO.getEmail(), code);

        return ResponseEntity.status(HttpStatus.OK).body("Code generated successfully!");
    }

    public void generateCodeForLogin(String to, String verificationCode) {
        String subject = "Your code for access is...";
        String body = "Please use the code " + verificationCode + " to login to the www.meusite.com/login platform.";

        this.emailService.sendEmail(to, subject, body);
    }

    public ResponseEntity<?> login(AuthDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.verificationCode());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
