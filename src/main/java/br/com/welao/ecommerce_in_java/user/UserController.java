package br.com.welao.ecommerce_in_java.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API for managements the Users.")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list-purchases-by-user-id/{userId}")
    @Operation(summary = "Lists the purchases of user.", description = "Lists the purchases of user by userId and returns your purchases.")
    public ResponseEntity<?> listPurchasesByUserId(@PathVariable long userId) {
        try {
            return this.userService.listPurchasesByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
