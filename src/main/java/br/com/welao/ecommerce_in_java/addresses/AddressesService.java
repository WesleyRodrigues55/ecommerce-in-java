package br.com.welao.ecommerce_in_java.addresses;

import br.com.welao.ecommerce_in_java.user.User;
import br.com.welao.ecommerce_in_java.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressesService {

    @Autowired
    private AddressesRepository addressesRepository;

    public ResponseEntity<?> create(AddressesDTO addressesDTO) {
        try {
            validAddressData(addressesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        Addresses address = AddressesMapper.toEntity(addressesDTO);
        this.addressesRepository.save(address);

        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }

    public ResponseEntity<?> update(long id, AddressesDTO addressesDTO) {
        Optional<Addresses> existingAddress = this.addressesRepository.findById(id);
        if (existingAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }

        Addresses address = existingAddress.get();

        try {
            addressesDTO.setUser(new User());
            addressesDTO.getUser().setId(existingAddress.get().getUser().getId());
            validAddressData(addressesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        Utils.copyNonNullProperties(addressesDTO, address);
        this.addressesRepository.save(address);

        return ResponseEntity.status(HttpStatus.OK).body("Address successfully updated");
    }

    private void validAddressData(AddressesDTO addressesDTO) {
        var existingAddressUser = this.addressesRepository.findByStreetAndNumberAndPostalCodeAndCityAndStateAndUserId(
                addressesDTO.getStreet(),
                addressesDTO.getNumber(),
                addressesDTO.getPostalCode(),
                addressesDTO.getCity(),
                addressesDTO.getState(),
                addressesDTO.getUser().getId()
        );

        if (existingAddressUser.isPresent()) {
            throw new RuntimeException("Address already exists");
        }
    }

    public ResponseEntity<?> list(long id) {
        Optional<Addresses> existingAddressUser = this.addressesRepository.findById(id);
        if (existingAddressUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }

        Addresses address = existingAddressUser.get();

        return ResponseEntity.status(HttpStatus.OK).body(address);

    }

    public ResponseEntity<?> listByIdUser(long idUser) {
        Optional<List<Optional<Addresses>>> existingAddressesUser = this.addressesRepository.findByUserId(idUser);
        if (existingAddressesUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Addresses not found");
        }

        List<Optional<Addresses>> address = existingAddressesUser.get();

        return ResponseEntity.status(HttpStatus.OK).body(address);
    }
}
