package br.com.welao.ecommerce_in_java.addresses;

public class AddressesMapper {
    public static Addresses toEntity(AddressesDTO dto) {
        Addresses addresses = new Addresses();

        addresses.setStreet(dto.getStreet());
        addresses.setCity(dto.getCity());
        addresses.setState(dto.getState());
        addresses.setPostalCode(dto.getPostalCode());
        addresses.setComplement(dto.getComplement());
        addresses.setNumber(dto.getNumber());
        addresses.setUser(dto.getUser());
        addresses.setOrderDetails(dto.getOrderDetails());

        return addresses;
    }

    public static AddressesDTO toDTO(Addresses addresses) {
        AddressesDTO dto = new AddressesDTO();

        dto.setStreet(addresses.getStreet());
        dto.setCity(addresses.getCity());
        dto.setState(addresses.getState());
        dto.setPostalCode(addresses.getPostalCode());
        dto.setComplement(addresses.getComplement());
        dto.setNumber(addresses.getNumber());
        dto.setUser(addresses.getUser());
        dto.setOrderDetails(addresses.getOrderDetails());

        return dto;
    }
}
