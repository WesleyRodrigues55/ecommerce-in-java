package br.com.welao.ecommerce_in_java.orderDetails;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order-details")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid OrderDetailsDTO orderDetailsDTO) {
        return this.orderDetailsService.create(orderDetailsDTO);
    }

    @GetMapping("/list-by-order-details-id/{orderDetailsId}")
    public ResponseEntity<?> listByOrderDetailsId(@PathVariable long orderDetailsId) {
        return this.orderDetailsService.listByOrderDetailsId(orderDetailsId);
    }
}
