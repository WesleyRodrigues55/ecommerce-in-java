package br.com.welao.ecommerce_in_java.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findById(long cartId);
    Cart findByPurchaseStatus(Boolean purchaseStatus);
    Optional<Cart> findByUserIdAndPurchaseStatus(long userId, Boolean purchaseStatus);
    List<Cart> findByUserId(long id);
}
