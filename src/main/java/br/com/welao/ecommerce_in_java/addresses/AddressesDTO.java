package br.com.welao.ecommerce_in_java.addresses;

import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class AddressesDTO {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String complement;
    private String number;
    private User user;
    private List<OrderDetails> orderDetails;
}
