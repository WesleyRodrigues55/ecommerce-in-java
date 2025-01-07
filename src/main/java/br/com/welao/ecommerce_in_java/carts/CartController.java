package br.com.welao.ecommerce_in_java.carts;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return this.cartService.list();
    }

    @PostMapping("/create-cart")
    public ResponseEntity<?> create(@RequestBody @Valid CartDTO cartDTO) {
        return this.cartService.create(cartDTO);
    }

}
