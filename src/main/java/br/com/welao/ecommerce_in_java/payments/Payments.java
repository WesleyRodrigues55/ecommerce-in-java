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

    private long amount;
    private String currency;
    private String paymentMethodType;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    private String paymentMethodId;
    private String paymentIntentId;
    private String paymentStatus;
    private String gateway;
    private String paymentToken;

    // card
    private String cardLastFourDigits;
    private String cardBrand;

    // Pix
    private String pixQrCode;
    private String pixTransactionId;
    private LocalDateTime pixExpirationDate;
    private String pixBankIssuer;

    // rate
    private float feeAmount;
    private String feeCurrency;

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
