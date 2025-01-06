package br.com.welao.ecommerce_in_java.products;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return this.productsService.list();
    }

    @GetMapping ("/list-paginated-items")
    public ResponseEntity<?> listPaginatedItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.productsService.listPaginatedItems(pageable);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> listByID(@PathVariable long id) {
        return this.productsService.listById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid ProductsDTO productsDTO) {
        return this.productsService.update(id, productsDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return this.productsService.delete(id);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<?> enable(@PathVariable long id) {
        return this.productsService.enable(id);
    }

}
