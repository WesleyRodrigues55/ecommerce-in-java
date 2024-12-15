package br.com.welao.ecommerce_in_java.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private String email;
    private String phone;
    private String cpf;
    private String cnpj;
    private String typePerson;
    private String verificationCode;
}
