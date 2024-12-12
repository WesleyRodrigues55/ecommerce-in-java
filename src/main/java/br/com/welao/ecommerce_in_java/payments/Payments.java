package br.com.welao.ecommerce_in_java.payments;


import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name="tb_payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String payment_method;

    private float amount;

    private LocalDateTime paymentDate;

    private String paymentStatus;

    private String transactionId;

    private String gateway;

    private String currency;

    // relation with order
    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderDetails orderDetails;
}
