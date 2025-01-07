package br.com.welao.ecommerce_in_java.itemsCart;

import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.products.Products;
import lombok.Data;

@Data
public class ItemsCartDTO {
    private int quantity;
    private float price;
    private String observation;
    private Cart cart;
    private Products products;
}
