package br.com.welao.ecommerce_in_java.carts;


import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name="tb_carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Boolean purchaseStatus;

    private float totalValue;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemsCart> itemsCart;

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private OrderDetails orderDetails;

}
