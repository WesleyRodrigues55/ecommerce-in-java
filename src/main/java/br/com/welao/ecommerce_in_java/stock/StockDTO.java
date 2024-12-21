package br.com.welao.ecommerce_in_java.stock;


import br.com.welao.ecommerce_in_java.products.Products;
import lombok.Data;

@Data
public class StockDTO {
    private String name;
    private int quantity;
    private float price;
    private Boolean active;
    private Products products;
}
