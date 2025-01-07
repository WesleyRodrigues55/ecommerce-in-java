package br.com.welao.ecommerce_in_java.carts;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Boolean purchaseStatus;
    private float totalValue;
    private User user;
    private List<ItemsCart> itemsCarts;
    private OrderDetails orderDetails;
}
