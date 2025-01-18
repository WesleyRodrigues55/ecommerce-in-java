package br.com.welao.ecommerce_in_java.addresses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("addresses")
public class AddressesController {

    @Autowired
    private AddressesService addressesService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid AddressesDTO addressesDTO) {
        try {
            return this.addressesService.create(addressesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid AddressesDTO addressesDTO) {
        try {
            return this.addressesService.update(id, addressesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> list(@PathVariable long id) {
        try {
            return this.addressesService.list(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list-addresses-user/{idUser}")
    public ResponseEntity<?> listByIdUser(@PathVariable long idUser) {
        try {
            return this.addressesService.listByIdUser(idUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
