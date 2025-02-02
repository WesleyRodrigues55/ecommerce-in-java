package br.com.welao.ecommerce_in_java.stock;

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
@RequestMapping("stock")
@Tag(name = "Stock", description = "API for management the stock.")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/register")
    @Operation(summary = "Registers an new item on stock", description = "Returns the item created in stock.")
    public ResponseEntity<?> register(@RequestBody @Valid StockDTO stockDTO) {
        try {
            return this.stockService.register(stockDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lists all items on stock.", description = "Returns all items on stock.")
    public ResponseEntity<?> list() {
        try {
            return this.stockService.list();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("list-paginated-items")
    @Operation(summary = "Lists all items on stock like a pagination.", description = "Returns all items on stock in a pagination.")
    public ResponseEntity<?> listPaginatedItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            return this.stockService.listPaginatedItems(pageable);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list/{id}")
    @Operation(summary = "Lists an item by id.", description = "Returns the item listed.")
    public ResponseEntity<?> list(@PathVariable long id) {
        try {
            return this.stockService.listById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Updates an item by id.", description = "Returns the item updated.")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid StockDTO stockDTO) {
        try {
            return this.stockService.update(id, stockDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Disables an item by id.", description = "The item is disable and returned.")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            return this.stockService.delete(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("enable/{id}")
    @Operation(summary = "Enables an item by id.", description = "The item is enable and returned.")
    public ResponseEntity<?> enable(@PathVariable long id) {
        try {
            return this.stockService.enable(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
