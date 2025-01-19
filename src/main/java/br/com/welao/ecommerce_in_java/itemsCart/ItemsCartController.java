package br.com.welao.ecommerce_in_java.itemsCart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("items-cart")
public class ItemsCartController {

    @Autowired
    private ItemsCartService itemsCartService;

    @PutMapping("/remove/{itemsCartId}")
    public ResponseEntity<?> remove(@PathVariable long itemsCartId) {
        try {
            return this.itemsCartService.remove(itemsCartId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list-by-user-id/{userId}")
    public ResponseEntity<?> listByUserId(@PathVariable long userId) {
        try {
            return this.itemsCartService.listByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
