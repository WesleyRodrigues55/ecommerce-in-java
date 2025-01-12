package br.com.welao.ecommerce_in_java.addresses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressesRepository extends JpaRepository<Addresses, Long> {
    Optional<Addresses> findByStreetAndNumberAndPostalCodeAndCityAndStateAndUserId(
            String street,
            String number,
            String postalCode,
            String city,
            String state,
            Long userId
    );
}
