package br.com.welao.ecommerce_in_java.stock;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid StockDTO stockDTO) {
        return this.stockService.register(stockDTO);
    }
}
