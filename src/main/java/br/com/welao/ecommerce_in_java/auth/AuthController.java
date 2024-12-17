package br.com.welao.ecommerce_in_java.auth;

import br.com.welao.ecommerce_in_java.user.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) {
        return this.authenticationService.register(userDTO);
    }

    @PostMapping("/send-code-email-for-login")
    public ResponseEntity<?> messageGenerateCodeForLogin(@RequestBody @Valid UserDTO userDTO) {
        return this.authenticationService.messageGenerateCodeForLogin(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authenticationDTO) {
        return this.authenticationService.login(authenticationDTO);
    }

}
