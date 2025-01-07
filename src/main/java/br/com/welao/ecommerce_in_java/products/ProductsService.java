package br.com.welao.ecommerce_in_java.products;

import br.com.welao.ecommerce_in_java.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    public ResponseEntity<?> register(ProductsDTO productsDTO) {

        if (this.productsRepository.findByName(productsDTO.getName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product name already exists");
        }

        Products product = ProductsMapper.toEntity(productsDTO);
        this.productsRepository.save(product);


        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    public ResponseEntity<?> list() {
        var products = this.productsRepository.findAllByActiveTrue();
        if (products == null || products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found");
        }

        // create pagination ?

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    public ResponseEntity<?> listPaginatedItems(Pageable pageable) {
        var products = this.productsRepository.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    public ResponseEntity<?> listById(long id) {
        var product = this.productsRepository.findById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        return ResponseEntity.ok().body(product);
    }

    public ResponseEntity<?> update(long id, ProductsDTO productsDTO) {
        var product = this.productsRepository.findById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Utils.copyNonNullProperties(productsDTO, product);
        this.productsRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    public ResponseEntity<?> delete(long id) {
        var product = this.productsRepository.findById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        product.setActive(false);
        this.productsRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body("product is deleted.");
    }

    public ResponseEntity<?> enable(long id) {
        var product = this.productsRepository.findById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        product.setActive(true);
        this.productsRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).body("product is deleted.");
    }
}
