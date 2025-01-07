package br.com.welao.ecommerce_in_java.carts;


import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name="tb_carts")
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Boolean purchaseStatus;

    private float totalValue;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "carts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemsCart> itemsCarts;

    @OneToOne(mappedBy = "carts", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderDetails orderDetails;

}
