package br.com.welao.ecommerce_in_java.products;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

    Products findByName(String name);
    Products findById(long id);
    List<Products> findAllByActiveTrue();
}
