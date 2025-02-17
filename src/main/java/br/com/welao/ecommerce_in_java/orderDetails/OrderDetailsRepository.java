package br.com.welao.ecommerce_in_java.orderDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findByCartId(long cartId);
}
