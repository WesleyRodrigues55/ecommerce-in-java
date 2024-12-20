package br.com.welao.ecommerce_in_java.products;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid ProductsDTO productsDTO) {
        return this.productsService.register(productsDTO);
    }

}
