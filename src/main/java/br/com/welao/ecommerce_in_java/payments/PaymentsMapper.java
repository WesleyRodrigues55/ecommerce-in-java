package br.com.welao.ecommerce_in_java.payments;

public class PaymentsMapper {
    public static Payments toEntity(PaymentsDTO dto) {
        Payments payments = new Payments();

        payments.setPaymentMethod(dto.getPaymentMethod());
        payments.setAmount(dto.getAmount());
        payments.setPaymentDate(dto.getPaymentDate());
        payments.setPaymentStatus(dto.getPaymentStatus());
        payments.setTransactionId(dto.getTransactionId());
        payments.setGateway(dto.getGateway());
        payments.setCurrency(dto.getCurrency());
        payments.setOrderDetails(dto.getOrderDetails());

        return payments;
    }

    public static PaymentsDTO toDTO(Payments payments) {
        PaymentsDTO dto = new PaymentsDTO();

        dto.setPaymentMethod(payments.getPaymentMethod());
        dto.setAmount(payments.getAmount());
        dto.setPaymentDate(payments.getPaymentDate());
        dto.setPaymentStatus(payments.getPaymentStatus());
        dto.setTransactionId(payments.getTransactionId());
        dto.setGateway(payments.getGateway());
        dto.setCurrency(payments.getCurrency());
        dto.setOrderDetails(payments.getOrderDetails());

        return dto;
    }
}
