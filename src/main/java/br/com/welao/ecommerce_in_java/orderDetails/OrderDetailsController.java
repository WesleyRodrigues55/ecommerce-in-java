package br.com.welao.ecommerce_in_java.orderDetails;

import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order-details")
@Tag(name = "Order Details", description = "API for managements the Order Details.")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @PostMapping("/create")
    @Operation(summary = "Creates an new orderDetails.", description = "Returns the orderDetails created.")
    public ResponseEntity<?> create(@RequestBody @Valid OrderDetailsDTO orderDetailsDTO) {
        try {
            return this.orderDetailsService.create(orderDetailsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{orderDetailsId}")
    @Operation(summary = "Updates the orderDetails by id.", description = "Check the payment status and close the orderDetails, cart and methodPayment.")
    public ResponseEntity<?> update(
            @PathVariable long orderDetailsId,
            @RequestBody @Valid String currency
    ) {
        try {
            return this.orderDetailsService.closeOrderDetailsAndFinishedPurchase(orderDetailsId, currency);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stripe error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }


    @GetMapping("/list-by-order-details-id/{orderDetailsId}")
    @Operation(summary = "Lists the orderDetails by id.", description = "Returns overview of orderDetails (purchase)")
    public ResponseEntity<?> listByOrderDetailsId(@PathVariable long orderDetailsId) {
        try {
            return this.orderDetailsService.listByOrderDetailsId(orderDetailsId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
