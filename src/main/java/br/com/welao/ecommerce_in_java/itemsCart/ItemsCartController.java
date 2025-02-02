package br.com.welao.ecommerce_in_java.itemsCart;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("items-cart")
@Tag(name = "Items Cart", description = "API for managements Items Cart")
public class ItemsCartController {

    @Autowired
    private ItemsCartService itemsCartService;

    @PutMapping("/remove/{itemsCartId}")
    @Operation(summary = "Removes an item from the Cart.", description = "Removes an item from the Cart and returns a message of successfully.")
    public ResponseEntity<?> remove(@PathVariable long itemsCartId) {
        try {
            return this.itemsCartService.remove(itemsCartId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list-by-user-id/{userId}")
    @Operation(summary = "Lists the items cart by userId.", description = "Returns the items cart by userId.")
    public ResponseEntity<?> listByUserId(@PathVariable long userId) {
        try {
            return this.itemsCartService.listByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
