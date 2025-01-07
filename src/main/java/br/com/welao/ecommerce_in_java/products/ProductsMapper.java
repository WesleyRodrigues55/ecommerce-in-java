package br.com.welao.ecommerce_in_java.products;

public class ProductsMapper {

    public static Products toEntity(ProductsDTO dto) {
        Products products = new Products();

        products.setName(dto.getName());
        products.setDescription(dto.getDescription());
        products.setPrice(dto.getPrice());
        products.setImage(dto.getImage());
        products.setActive(dto.getActive());

        return products;
    }

    public static ProductsDTO toDto(Products products) {
        ProductsDTO dto = new ProductsDTO();

        dto.setName(products.getName());
        dto.setDescription(products.getDescription());
        dto.setPrice(products.getPrice());
        dto.setImage(products.getImage());
        dto.setActive(products.getActive());

        return dto;
    }
}
