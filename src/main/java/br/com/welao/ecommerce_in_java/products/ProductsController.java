package br.com.welao.ecommerce_in_java.products;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid ProductsDTO productsDTO) {
        return this.productsService.register(productsDTO);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> listByID(@PathVariable int id) {
        return this.productsService.listById(id);
    }

}
