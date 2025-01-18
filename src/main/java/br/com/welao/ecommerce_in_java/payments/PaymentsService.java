package br.com.welao.ecommerce_in_java.payments;

import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetailsRepository;
import br.com.welao.ecommerce_in_java.user.User;
import br.com.welao.ecommerce_in_java.user.UserRepository;
import br.com.welao.ecommerce_in_java.user.UserService;
import br.com.welao.ecommerce_in_java.utils.Utils;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentsService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public ResponseEntity<?> createPaymentMethod(PaymentsDTO paymentsDTO) {
        long userId = paymentsDTO.getUser().getId();
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        paymentsDTO.setGateway("STRIPE");
        Payments payments = PaymentsMapper.toEntity(paymentsDTO);
        Payments savedPayment = this.paymentsRepository.save(payments);

        Map<String, Object> response = new HashMap<>();
        response.put("payment", "Payment method created successfully!");
        response.put("paymentId", savedPayment.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<?> createPaymentIntent(
            long paymentId,
            PaymentsDTO paymentsDTO
    ) throws StripeException {

        Optional<Payments> existingPayment = this.paymentsRepository.findById(paymentId);
        if (existingPayment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }

        if (paymentsDTO.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid amount");
        }
        if (paymentsDTO.getCurrency() == null || paymentsDTO.getCurrency().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Currency is required");
        }

        Payments payment = existingPayment.get();
        String paymentMethodId = payment.getPaymentMethodId();

        // create payment intent
        PaymentIntent paymentIntent = createStripPaymentIntent(paymentsDTO, paymentMethodId);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());

        paymentsDTO.setPaymentStatus(paymentIntent.getStatus());
        paymentsDTO.setPaymentIntentId(paymentIntent.getId());
        paymentsDTO.setPaymentToken(paymentIntent.getClientSecret());

        Utils.copyNonNullProperties(paymentsDTO, payment);
        this.paymentsRepository.save(payment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private PaymentIntent createStripPaymentIntent(
            PaymentsDTO paymentsDTO,
            String paymentMethodId
    ) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(paymentsDTO.getAmount())
                        .setCurrency(paymentsDTO.getCurrency())
                        .setPaymentMethod(paymentMethodId)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .putExtraParam("allow_redirects", "never")
                                        .build()
                        )
                        .setConfirm(true)
                        .build();

        return PaymentIntent.create(params);
    }


    /*

        IF IT CONTAINS THE PIX METHOD TYPE, USE THE METHOD BELOW

     */
//    public ResponseEntity<?> getPaymentStatus(String paymentIntentId, String sigHeader) throws StripeException {
//        Stripe.apiKey = stripeApiKey;
//
//        Event event = null;
//
//        try {
//            event = Webhook.constructEvent(
//                    paymentIntentId, sigHeader, stripeApiKey
//            );
//        } catch (RuntimeException e) {
//            // Invalid payload
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        } catch (SignatureVerificationException e) {
//            // Invalid signature
//           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//
//        PaymentIntent intent = (PaymentIntent) event
//                .getDataObjectDeserializer()
//                .getObject()
//                .get();
//
//        switch(event.getType()) {
//            case "payment_intent.succeeded":
//                System.out.println("Succeeded: " + intent.getId());
//                break;
//
//            case "payment_intent.payment_failed":
//                System.out.println("Failed: " + intent.getId());
//                break;
//
//            default:
//                break;
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(event);
//    }


}
