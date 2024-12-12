package br.com.welao.ecommerce_in_java.itemsCart;

import br.com.welao.ecommerce_in_java.carts.Carts;
import br.com.welao.ecommerce_in_java.products.Products;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="tb_items_cart")
public class ItemsCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantity;

    private float price;

    private String observation;


    // relation with cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Carts cart;

    // relation with product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;
}
