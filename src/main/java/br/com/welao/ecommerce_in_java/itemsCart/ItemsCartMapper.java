package br.com.welao.ecommerce_in_java.itemsCart;

public class ItemsCartMapper {
    public static ItemsCart toEntity(ItemsCartDTO dto) {
        ItemsCart itemsCarts = new ItemsCart();

        itemsCarts.setQuantity(dto.getQuantity());
        itemsCarts.setPrice(dto.getPrice());
        itemsCarts.setObservation(dto.getObservation());
        itemsCarts.setCarts(dto.getCarts());
        itemsCarts.setProducts(dto.getProducts());

        return itemsCarts;
    }

    public static ItemsCartDTO toDto(ItemsCart itemsCart) {
        ItemsCartDTO dto = new ItemsCartDTO();

        dto.setQuantity(itemsCart.getQuantity());
        dto.setPrice(itemsCart.getPrice());
        dto.setObservation(itemsCart.getObservation());
        dto.setCarts(itemsCart.getCarts());
        dto.setProducts(itemsCart.getProducts());

        return dto;
    }
}
