package br.com.welao.ecommerce_in_java.stock;

import br.com.welao.ecommerce_in_java.products.Products;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="tb_stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;

    private int quantity;

    private float price;

    private Boolean active;

    // relation with product
    @OneToOne
    @JoinColumn(name = "product_id")
    private Products products;
}
