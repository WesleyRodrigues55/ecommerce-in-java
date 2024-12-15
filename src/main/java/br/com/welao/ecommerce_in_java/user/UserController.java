package br.com.welao.ecommerce_in_java.user;

import br.com.welao.ecommerce_in_java.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-account")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @PostMapping("/validate-code-for-login")
    public ResponseEntity<?> validateCodeForAccessAndCreateAuth(@RequestBody UserDTO userDTO) {
        return userService.validateCodeForAccessAndCreateAuth(userDTO);
    }

}