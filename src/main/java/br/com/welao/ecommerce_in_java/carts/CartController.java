package br.com.welao.ecommerce_in_java.carts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
@Tag(name = "Cart", description = "API fro management the Cart.")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/create")
    @Operation(summary = "Creates an new Cart for a user.", description = "Creates a new cart for a user when an item is added to the cart, if a cart already exists, an item is added to the open cart.")
    public ResponseEntity<?> create(@RequestBody @Valid CartDTO cartDTO) {
        try {
            return this.cartService.create(cartDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
