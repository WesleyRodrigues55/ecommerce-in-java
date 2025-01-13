package br.com.welao.ecommerce_in_java.payments;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid PaymentsDTO paymentsDTO) {
        return this.paymentsService.create(paymentsDTO);
    }
}
