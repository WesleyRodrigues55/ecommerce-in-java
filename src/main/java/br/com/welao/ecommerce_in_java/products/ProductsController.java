package br.com.welao.ecommerce_in_java.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
@Tag(name = "Products", description = "API for management the Products.")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @PostMapping("/register")
    @Operation(summary = "Register a new item on product", description = "Returns the item created in product.")
    public ResponseEntity<?> register(@RequestBody @Valid ProductsDTO productsDTO) {
        try {
            return this.productsService.register(productsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "List all items on product.", description = "Returns all items on product.")
    public ResponseEntity<?> list() {
        try {
            return this.productsService.list();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping ("/list-paginated-items")
    @Operation(summary = "Lists all items on product like a pagination.", description = "Returns all items on product in a pagination.")
    public ResponseEntity<?> listPaginatedItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            return this.productsService.listPaginatedItems(pageable);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list/{id}")
    @Operation(summary = "Lists an item by id.", description = "Returns the item listed.")
    public ResponseEntity<?> listByID(@PathVariable long id) {
        try {
            return this.productsService.listById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Updates an item by id.", description = "Returns the item updated.")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid ProductsDTO productsDTO) {
        try {
            return this.productsService.update(id, productsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Disables an item by id.", description = "The item is disable and returned.")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            return this.productsService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/enable/{id}")
    @Operation(summary = "Enables an item by id.", description = "The item is enable and returned.")
    public ResponseEntity<?> enable(@PathVariable long id) {
        try {
            return this.productsService.enable(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
