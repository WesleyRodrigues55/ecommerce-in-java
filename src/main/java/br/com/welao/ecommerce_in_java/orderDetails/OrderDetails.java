package br.com.welao.ecommerce_in_java.orderDetails;

import br.com.welao.ecommerce_in_java.addresses.Addresses;
import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.payments.Payments;
import br.com.welao.ecommerce_in_java.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name="tb_order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private float totalValue;

    private LocalDateTime orderDate;

    private String orderStatus;

    // relation with user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // relation with address
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Addresses addresses;

    // relation with cart
    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // relation with payment
    @OneToOne(mappedBy = "orderDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payments payments;

}
