package br.com.welao.ecommerce_in_java.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;
}
