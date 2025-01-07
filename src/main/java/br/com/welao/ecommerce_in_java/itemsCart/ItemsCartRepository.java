package br.com.welao.ecommerce_in_java.itemsCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsCartRepository extends JpaRepository<ItemsCart, Long> {

}
