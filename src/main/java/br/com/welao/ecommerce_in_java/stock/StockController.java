package br.com.welao.ecommerce_in_java.stock;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid StockDTO stockDTO) {
        return this.stockService.register(stockDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return this.stockService.list();
    }

    @GetMapping("list-paginated-items")
    public ResponseEntity<?> listPaginatedItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.stockService.listPaginatedItems(pageable);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> list(@PathVariable long id) {
        return this.stockService.listById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid StockDTO stockDTO) {
        return this.stockService.update(id, stockDTO);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return this.stockService.delete(id);
    }

    @PutMapping("enable/{id}")
    public ResponseEntity<?> enable(@PathVariable long id) {
        return this.stockService.enable(id);
    }
}
