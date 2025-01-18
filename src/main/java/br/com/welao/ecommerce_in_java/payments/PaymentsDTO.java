package br.com.welao.ecommerce_in_java.payments;

import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentsDTO {

    private long amount;
    private String currency;
    private String paymentMethodType;
    private LocalDateTime paymentDate;
    private String paymentStatus;
    private String paymentMethodId;
    private String paymentIntentId;
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

    private OrderDetails orderDetails;
    private User user;
}
