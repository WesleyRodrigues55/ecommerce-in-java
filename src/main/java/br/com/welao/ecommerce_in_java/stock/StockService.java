package br.com.welao.ecommerce_in_java.stock;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.products.ProductsRepository;
import br.com.welao.ecommerce_in_java.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<Stock> stockOptional = this.stockRepository.findById(id);
        if (stockOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }
        Stock stock = stockOptional.get();

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public ResponseEntity<?> update(long id, StockDTO stockDTO) {
        Optional<Stock> stockOptional = this.stockRepository.findById(id);
        if (stockOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }
        Stock stock = stockOptional.get();

        if (this.stockRepository.findByName(stockDTO.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name already exists");
        }

        Utils.copyNonNullProperties(stockDTO, stock);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public ResponseEntity<?> delete(long id) {
        Optional<Stock> stockOptional = this.stockRepository.findById(id);
        if (stockOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }
        Stock stock = stockOptional.get();

        stock.setActive(false);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public ResponseEntity<?> enable(long id) {
        Optional<Stock> stockOptional = this.stockRepository.findById(id);
        if (stockOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No stock found");
        }
        Stock stock = stockOptional.get();

        stock.setActive(true);
        this.stockRepository.save(stock);

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    public void addsOneMoreToTheStock(ItemsCart hasItem) {
        var productId = hasItem.getProducts().getId();
        var stock = this.stockRepository.findByProductsId(productId);
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }

        stock.setQuantity(stock.getQuantity() + 1);
        this.stockRepository.save(stock);
    }
}
