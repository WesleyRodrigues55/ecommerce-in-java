package br.com.welao.ecommerce_in_java.products;

import br.com.welao.ecommerce_in_java.itemsCart.ItemsCart;
import br.com.welao.ecommerce_in_java.stock.Stock;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name="tb_products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    private String description;

    private float price;

    private String image;

    private Boolean active;

    @OneToOne(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemsCart> itemsCart;

}
