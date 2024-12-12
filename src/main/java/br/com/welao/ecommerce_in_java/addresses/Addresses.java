package br.com.welao.ecommerce_in_java.addresses;


import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name="tb_addresses")
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String complement;

    private String number;

    //relation with user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;

}
