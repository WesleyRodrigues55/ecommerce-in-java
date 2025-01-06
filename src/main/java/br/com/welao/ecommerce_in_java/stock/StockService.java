package br.com.welao.ecommerce_in_java.stock;

import br.com.welao.ecommerce_in_java.products.ProductsRepository;
import br.com.welao.ecommerce_in_java.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductsRepository productsRepository;

    public ResponseEntity<?> register(StockDTO stockDTO) {
        var productId = productsRepository.findById(stockDTO.getProducts().getId());
        if (productId == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product id not found");
        }

        if (this.stockRepository.findByName(stockDTO.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exists");
        }

        try {
            Stock stock = StockMapper.toEntity(stockDTO);
            this.stockRepository.save(stock);

            return ResponseEntity.status(HttpStatus.CREATED).body(stock);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The product is already registered in stock");
        }
    }

    public ResponseEntity<?> list() {
        var stocks = this.stockRepository.findByActiveTrue();
        if (stocks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stocks found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    public ResponseEntity<?> listPaginatedItems(Pageable pageable) {
        var stocks = this.stockRepository.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }

    public ResponseEntity<?> listById(long id) {
        var stock = this.stockRepository.findById(id);
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public ResponseEntity<?> update(long id, StockDTO stockDTO) {
        var stock = this.stockRepository.findById(id);
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }

        if (this.stockRepository.findByName(stockDTO.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exists");
        }

        Utils.copyNonNullProperties(stockDTO, stock);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public ResponseEntity<?> delete(long id) {
        var stock = this.stockRepository.findById(id);
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }

        stock.setActive(false);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public ResponseEntity<?> enable(long id) {
        var stock = this.stockRepository.findById(id);
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }

        stock.setActive(true);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }
}
