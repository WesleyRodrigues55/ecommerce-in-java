package br.com.welao.ecommerce_in_java.user;


import br.com.welao.ecommerce_in_java.addresses.Addresses;
import br.com.welao.ecommerce_in_java.carts.Carts;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name="tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String typePerson;

//  Natural person
    @Column(unique=true)
    private String CPF;
    private String fullName;

//    Legal Entity
    @Column(unique=true)
    private String CNPJ;
    private String corporateName;
    private String tradeName;
    private String contactPerson;
    private String stateRegistration;
    private Boolean isImmune;

    @Column(unique=true)
    private String phone;

    @Column(unique=true)
    private String email;

    private String verificationCode;

    private LocalDateTime codeExpiresAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Addresses> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Carts> carts;

}

