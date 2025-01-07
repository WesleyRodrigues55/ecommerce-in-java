package br.com.welao.ecommerce_in_java.itemsCart;

import br.com.welao.ecommerce_in_java.carts.Carts;
import br.com.welao.ecommerce_in_java.products.Products;
import lombok.Data;

@Data
public class ItemsCartDTO {
    private int quantity;
    private float price;
    private String observation;
    private Carts carts;
    private Products products;
}
