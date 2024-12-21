package br.com.welao.ecommerce_in_java.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    // check relation with Products
    public ResponseEntity<?> register(StockDTO stockDTO) {
        if (this.stockRepository.findByName(stockDTO.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exists");
        }

        Stock stock = StockMapper.toEntity(stockDTO);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.CREATED).body(stock);
    }

    // create list stock

    // create list stock by id

    // create update stock

    // create disable stock
}
