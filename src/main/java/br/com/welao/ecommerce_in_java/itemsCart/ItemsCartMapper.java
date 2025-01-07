package br.com.welao.ecommerce_in_java.itemsCart;

public class ItemsCartMapper {
    public static ItemsCart toEntity(ItemsCartDTO dto) {
        ItemsCart itemsCart = new ItemsCart();

        itemsCart.setQuantity(dto.getQuantity());
        itemsCart.setPrice(dto.getPrice());
        itemsCart.setObservation(dto.getObservation());
        itemsCart.setCart(dto.getCart());
        itemsCart.setProducts(dto.getProducts());

        return itemsCart;
    }

    public static ItemsCartDTO toDto(ItemsCart itemsCart) {
        ItemsCartDTO dto = new ItemsCartDTO();

        dto.setQuantity(itemsCart.getQuantity());
        dto.setPrice(itemsCart.getPrice());
        dto.setObservation(itemsCart.getObservation());
        dto.setCart(itemsCart.getCart());
        dto.setProducts(itemsCart.getProducts());

        return dto;
    }
}
