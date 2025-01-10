package br.com.welao.ecommerce_in_java.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByPurchaseStatus(Boolean purchaseStatus);
    List<Cart> findByUserId(Long id);
    Cart findById(long id);

}
