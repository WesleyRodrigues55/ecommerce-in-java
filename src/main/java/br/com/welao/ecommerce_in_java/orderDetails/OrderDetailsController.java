package br.com.welao.ecommerce_in_java.orderDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order-details")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;
}
