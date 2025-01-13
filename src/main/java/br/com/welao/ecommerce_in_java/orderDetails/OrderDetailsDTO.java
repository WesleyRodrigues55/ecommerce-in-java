package br.com.welao.ecommerce_in_java.orderDetails;

import br.com.welao.ecommerce_in_java.addresses.Addresses;
import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.payments.Payments;
import br.com.welao.ecommerce_in_java.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailsDTO {
    private float totalValue;
    private LocalDateTime orderDate;
    private String orderStatus;
    private User user;
    private Addresses addresses;
    private Cart cart;
    private Payments payments;
}
