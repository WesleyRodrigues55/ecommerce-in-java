package br.com.welao.ecommerce_in_java.products;

import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<?> listById(long id) {
        var product = this.productsRepository.findById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        return ResponseEntity.ok().body(product);

    }

}
