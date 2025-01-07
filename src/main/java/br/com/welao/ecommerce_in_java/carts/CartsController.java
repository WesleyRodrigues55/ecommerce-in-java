package br.com.welao.ecommerce_in_java.carts;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
public class CartsController {

    @Autowired
    private CartsService cartsService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return this.cartsService.list();
    }

    @PostMapping("/create-cart")
    public ResponseEntity<?> create(@RequestBody @Valid CartsDTO cartsDTO) {
        return this.cartsService.create(cartsDTO);
    }

}
