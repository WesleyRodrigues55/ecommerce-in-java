package br.com.welao.ecommerce_in_java.payments;

import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @PostMapping("/create-payment-method")
    public ResponseEntity<?> createPaymentMethod(@RequestBody @Valid PaymentsDTO paymentsDTO) {
        try {
            return this.paymentsService.createPaymentMethod(paymentsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

//    @PostMapping("/create-payment-intent/{paymentId}/{orderDetailsID}")
//    public ResponseEntity<?> createPaymentIntent(
//            @PathVariable long paymentId,
//            @PathVariable long orderDetailsID,
//            @RequestBody @Valid PaymentsDTO paymentsDTO)
//    {
//        try {
//            return paymentsService.createPaymentIntent(paymentId, orderDetailsID, paymentsDTO);
//        } catch (StripeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stripe error: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
//        }
//    }

    /*

        IF IT CONTAINS THE PIX METHOD TYPE, USE THE METHOD BELOW

     */
//    @PostMapping("/webhook")
//    public ResponseEntity<?> getStatus(
//            @RequestBody @Valid String paymentIntentId,
//            @RequestHeader("Stripe-Signature") String sigHeader
//    ) {
//        try {
//            return this.paymentsService.getPaymentStatus(paymentIntentId, sigHeader);
//        } catch (Exception e) {
//            throw new RuntimeException("Error capturing payment status: " + e.getMessage());
//        }
//    }
}
