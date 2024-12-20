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


        return ResponseEntity.ok().build();
    }

}
