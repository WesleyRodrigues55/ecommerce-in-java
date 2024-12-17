package br.com.welao.ecommerce_in_java.user;

public class UserMapper {
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setCpf(dto.getCpf());
        user.setCnpj(dto.getCnpj());
        user.setTypePerson(dto.getTypePerson());
        user.setVerificationCode(dto.getVerificationCode());

        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setRole(user.getRole());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCpf(user.getCpf());
        dto.setCnpj(user.getCnpj());
        dto.setTypePerson(user.getTypePerson());
        dto.setVerificationCode(user.getVerificationCode());

        return dto;
    }
}
