package br.com.welao.ecommerce_in_java.addresses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("addresses")
public class AddressesController {

    @Autowired
    private AddressesService addressesService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid AddressesDTO addressesDTO) {
        return this.addressesService.create(addressesDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody @Valid AddressesDTO addressesDTO) {
        return this.addressesService.update(id, addressesDTO);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> list(@PathVariable long id) {
        return this.addressesService.list(id);
    }

    @GetMapping("/list-addresses-user/{idUser}")
    public ResponseEntity<?> listByIdUser(@PathVariable long idUser) {
        return this.addressesService.listByIdUser(idUser);
    }

}
