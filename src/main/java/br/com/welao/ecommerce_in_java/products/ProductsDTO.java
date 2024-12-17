package br.com.welao.ecommerce_in_java.products;

import lombok.Data;

@Data
public class ProductsDTO {
    private String name;
    private String description;
    private float price;
    private String image;
    private Boolean active;
}
