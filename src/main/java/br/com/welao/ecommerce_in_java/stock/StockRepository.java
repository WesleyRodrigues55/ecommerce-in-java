package br.com.welao.ecommerce_in_java.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findByName(String name);
    Stock findByProductsId(long productId);
    List<Stock> findByActiveTrue();
}
