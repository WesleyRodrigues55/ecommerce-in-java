package br.com.welao.ecommerce_in_java.orderDetails;

public class OrderDetailsMapper {

    public static OrderDetails toEntity(OrderDetailsDTO dto) {
        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setTotalValue(dto.getTotalValue());
        orderDetails.setOrderDate(dto.getOrderDate());
        orderDetails.setOrderStatus(dto.getOrderStatus());
        orderDetails.setUser(dto.getUser());
        orderDetails.setAddresses(dto.getAddresses());
        orderDetails.setCart(dto.getCart());
        orderDetails.setPayments(dto.getPayments());

        return orderDetails;
    }

    public static OrderDetailsDTO toDTO(OrderDetails orderDetails) {
        OrderDetailsDTO dto = new OrderDetailsDTO();

        dto.setTotalValue(orderDetails.getTotalValue());
        dto.setOrderDate(orderDetails.getOrderDate());
        dto.setOrderStatus(orderDetails.getOrderStatus());
        dto.setUser(orderDetails.getUser());
        dto.setAddresses(orderDetails.getAddresses());
        dto.setCart(orderDetails.getCart());
        dto.setPayments(orderDetails.getPayments());

        return dto;
    }

}
