package br.com.welao.ecommerce_in_java.payments;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @PostMapping("/create-payment-method")
    public ResponseEntity<?> createPaymentMethod(@RequestBody @Valid PaymentsDTO paymentsDTO) {
        return this.paymentsService.createPaymentMethod(paymentsDTO);
    }

    @PostMapping("/create-payment-intent/{paymentId}")
    public ResponseEntity<?> createPaymentIntent(@PathVariable long paymentId, @RequestBody @Valid PaymentsDTO paymentsDTO) {
        try {
            return paymentsService.createPaymentIntent(paymentId, paymentsDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o pagamento: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> getStatus(
            @RequestBody @Valid String paymentIntentId,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        try {
            return this.paymentsService.getPaymentStatus(paymentIntentId, sigHeader);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao capturar o status pagamento: " + e.getMessage());
        }
    }
}
