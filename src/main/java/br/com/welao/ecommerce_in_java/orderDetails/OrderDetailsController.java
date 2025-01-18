package br.com.welao.ecommerce_in_java.orderDetails;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order-details")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid OrderDetailsDTO orderDetailsDTO) {
        try {
            return this.orderDetailsService.create(orderDetailsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list-by-order-details-id/{orderDetailsId}")
    public ResponseEntity<?> listByOrderDetailsId(@PathVariable long orderDetailsId) {
        try {
            return this.orderDetailsService.listByOrderDetailsId(orderDetailsId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
