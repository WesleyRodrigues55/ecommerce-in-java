package br.com.welao.ecommerce_in_java.addresses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("addresses")
@Tag(name = "Addresses", description = "API for management the Addresses.")
public class AddressesController {

    @Autowired
    private AddressesService addressesService;

    @PostMapping("/create")
    @Operation(summary = "Creates an new address for the user.", description = "Returns the address created.")
    public ResponseEntity<?> create(@RequestBody @Valid AddressesDTO addressesDTO) {
        try {
            return this.addressesService.create(addressesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Updates an existing address.", description = "Updates an address and returns the request status.")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid AddressesDTO addressesDTO) {
        try {
            return this.addressesService.update(id, addressesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list/{id}")
    @Operation(summary = "Lists an address by id.", description = "Returns the address filtered by id.")
    public ResponseEntity<?> list(@PathVariable long id) {
        try {
            return this.addressesService.list(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list-addresses-user/{idUser}")
    @Operation(summary = "Lists all address by userId.", description = "Returns all addresses of user.")
    public ResponseEntity<?> listByIdUser(@PathVariable long idUser) {
        try {
            return this.addressesService.listByIdUser(idUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
