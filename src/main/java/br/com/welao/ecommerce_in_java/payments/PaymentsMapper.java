package br.com.welao.ecommerce_in_java.payments;

public class PaymentsMapper {
    public static Payments toEntity(PaymentsDTO dto) {
        Payments payments = new Payments();

        payments.setAmount(dto.getAmount());
        payments.setCurrency(dto.getCurrency());
        payments.setPaymentMethodType(dto.getPaymentMethodType());
        payments.setPaymentDate(dto.getPaymentDate());
        payments.setPaymentStatus(dto.getPaymentStatus());
        payments.setPaymentMethodId(dto.getPaymentMethodId());
        payments.setPaymentIntentId(dto.getPaymentIntentId());
        payments.setGateway(dto.getGateway());
        payments.setPaymentToken(dto.getPaymentToken());

        payments.setCardLastFourDigits(dto.getCardLastFourDigits());
        payments.setCardBrand(dto.getCardBrand());

        payments.setPixQrCode(dto.getPixQrCode());
        payments.setPixTransactionId(dto.getPixTransactionId());
        payments.setPixExpirationDate(dto.getPixExpirationDate());
        payments.setPixBankIssuer(dto.getPixBankIssuer());

        payments.setFeeAmount(dto.getFeeAmount());
        payments.setFeeCurrency(dto.getFeeCurrency());

        payments.setOrderDetails(dto.getOrderDetails());
        payments.setUser(dto.getUser());

        return payments;
    }

    public static PaymentsDTO toDTO(Payments payments) {
        PaymentsDTO dto = new PaymentsDTO();

        dto.setAmount(payments.getAmount());
        dto.setCurrency(payments.getCurrency());
        dto.setPaymentMethodType(payments.getPaymentMethodType());
        dto.setPaymentDate(payments.getPaymentDate());
        dto.setPaymentStatus(payments.getPaymentStatus());
        dto.setPaymentMethodId(payments.getPaymentMethodId());
        dto.setPaymentIntentId(payments.getPaymentIntentId());
        dto.setGateway(payments.getGateway());
        dto.setPaymentToken(payments.getPaymentToken());

        dto.setCardLastFourDigits(payments.getCardLastFourDigits());
        dto.setCardBrand(payments.getCardBrand());

        dto.setPixQrCode(payments.getPixQrCode());
        dto.setPixTransactionId(payments.getPixTransactionId());
        dto.setPixExpirationDate(payments.getPixExpirationDate());
        dto.setPixBankIssuer(payments.getPixBankIssuer());

        dto.setFeeAmount(payments.getFeeAmount());
        dto.setFeeCurrency(payments.getFeeCurrency());

        dto.setOrderDetails(payments.getOrderDetails());
        dto.setUser(payments.getUser());

        return dto;
    }
}
