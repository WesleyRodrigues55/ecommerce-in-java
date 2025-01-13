package br.com.welao.ecommerce_in_java.itemsCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsCartRepository extends JpaRepository<ItemsCart, Long> {
    List<Optional<ItemsCart>> findByCartId(Long cartId);
}
