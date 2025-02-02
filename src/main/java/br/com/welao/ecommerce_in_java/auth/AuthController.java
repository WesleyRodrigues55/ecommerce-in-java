package br.com.welao.ecommerce_in_java.auth;

import br.com.welao.ecommerce_in_java.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Tag(name = "Auth", description = "API for management the Auth.")
public class AuthController {

    @Autowired
    private AuthService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Registers a new user.", description = "Creates a new user and returns status")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) {
        try {
            return this.authenticationService.register(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/send-code-email-for-login")
    @Operation(summary = "Sends an code of access for the email of user.", description = "Generates an code of access for the user to be authenticate.")
    public ResponseEntity<?> messageGenerateCodeForLogin(@RequestBody @Valid UserDTO userDTO) {
        try {
            return this.authenticationService.messageGenerateCodeForLogin(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Auths user for access and generate token.", description = "Checks the email and the generated code for access, and finally returns a token for access.")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authenticationDTO) {
        try {
            return this.authenticationService.login(authenticationDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
