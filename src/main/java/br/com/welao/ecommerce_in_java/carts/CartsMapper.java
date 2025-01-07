package br.com.welao.ecommerce_in_java.carts;

public class CartsMapper {
    public static Carts toEntity(CartsDTO dto) {
        Carts carts = new Carts();

        carts.setPurchaseStatus(dto.getPurchaseStatus());
        carts.setTotalValue(dto.getTotalValue());
        carts.setUser(dto.getUser());
        carts.setItemsCarts(dto.getItemsCarts());
        carts.setOrderDetails(dto.getOrderDetails());

        return carts;
    }

    public static CartsDTO toDto(Carts carts) {
        CartsDTO dto = new CartsDTO();

        dto.setPurchaseStatus(carts.getPurchaseStatus());
        dto.setTotalValue(carts.getTotalValue());
        dto.setUser(carts.getUser());
        dto.setItemsCarts(carts.getItemsCarts());
        dto.setOrderDetails(carts.getOrderDetails());

        return dto;
    }
}
