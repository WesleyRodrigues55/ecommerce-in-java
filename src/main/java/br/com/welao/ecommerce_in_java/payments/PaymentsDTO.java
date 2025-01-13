package br.com.welao.ecommerce_in_java.payments;

import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentsDTO {
    private String paymentMethod;
    private float amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
    private String transactionId;
    private String gateway;
    private String paymentToken;
    private String cardLastFourDigits;
    private String cardBrand;
    private OrderDetails orderDetails;
    private User user;
}
