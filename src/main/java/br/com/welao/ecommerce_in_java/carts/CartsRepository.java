package br.com.welao.ecommerce_in_java.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Long> {
    Carts findByPurchaseStatus(Boolean purchaseStatus);
    List<Carts> findByUserId(Long id);
}
