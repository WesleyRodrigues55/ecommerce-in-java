package br.com.welao.ecommerce_in_java.itemsCart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("items-cart")
public class ItemsCartController {

    @Autowired
    private ItemsCartService itemsCartService;

    @PutMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable long id) {
        return this.itemsCartService.remove(id);
    }

    @GetMapping("/list-by-user-id/{userId}")
    public ResponseEntity<?> listByUserId(@PathVariable long userId) {
        return this.itemsCartService.listByUserId(userId);
    }
}
