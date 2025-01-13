package br.com.welao.ecommerce_in_java.payments;


import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity(name="tb_payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String paymentMethod;

    private float amount;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    private String paymentStatus;

    private String transactionId;

    private String gateway;

    private String paymentToken;

    private String cardLastFourDigits;

    private String cardBrand;

    // relation with user
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // relation with payment
    @OneToOne(mappedBy = "payments", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private OrderDetails orderDetails;
}
