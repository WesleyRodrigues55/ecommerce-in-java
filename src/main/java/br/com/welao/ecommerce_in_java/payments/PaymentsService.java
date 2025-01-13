package br.com.welao.ecommerce_in_java.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;
}
