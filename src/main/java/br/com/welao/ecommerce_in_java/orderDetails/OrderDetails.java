package br.com.welao.ecommerce_in_java.orderDetails;

import br.com.welao.ecommerce_in_java.addresses.Addresses;
import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.payments.Payments;
import br.com.welao.ecommerce_in_java.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name="tb_order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private float totalValue;

    @CreationTimestamp
    private LocalDateTime orderDate;

    private String orderStatus;

    // relation with user
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // relation with address
    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonIgnore
    private Addresses addresses;

    // relation with cart
    @OneToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    // relation with payments
    @OneToOne
    @JoinColumn(name = "payments_id")
    @JsonIgnore
    private Payments payments;



}
