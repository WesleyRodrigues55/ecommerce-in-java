package br.com.welao.ecommerce_in_java.carts;

public class CartMapper {
    public static Cart toEntity(CartDTO dto) {
        Cart cart = new Cart();

        cart.setPurchaseStatus(dto.getPurchaseStatus());
        cart.setTotalValue(dto.getTotalValue());
        cart.setUser(dto.getUser());
        cart.setItemsCart(dto.getItemsCarts());
        cart.setOrderDetails(dto.getOrderDetails());

        return cart;
    }

    public static CartDTO toDto(Cart cart) {
        CartDTO dto = new CartDTO();

        dto.setPurchaseStatus(cart.getPurchaseStatus());
        dto.setTotalValue(cart.getTotalValue());
        dto.setUser(cart.getUser());
        dto.setItemsCarts(cart.getItemsCart());
        dto.setOrderDetails(cart.getOrderDetails());

        return dto;
    }
}
