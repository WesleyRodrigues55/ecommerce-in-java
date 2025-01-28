package br.com.welao.ecommerce_in_java.payments;

import br.com.welao.ecommerce_in_java.carts.Cart;
import br.com.welao.ecommerce_in_java.carts.CartDTO;
import br.com.welao.ecommerce_in_java.carts.CartRepository;
import br.com.welao.ecommerce_in_java.carts.CartService;
import br.com.welao.ecommerce_in_java.email.EmailService;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetails;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetailsDTO;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetailsRepository;
import br.com.welao.ecommerce_in_java.orderDetails.OrderDetailsService;
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
import org.springframework.web.server.ResponseStatusException;

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
    private EmailService emailService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public ResponseEntity<?> createPaymentMethod(PaymentsDTO paymentsDTO) {
        try {
            validIfUserExistingByUserId(paymentsDTO.getUser().getId(), paymentsDTO);

            paymentsDTO.setGateway("STRIPE");
            Payments payments = PaymentsMapper.toEntity(paymentsDTO);
            Payments savedPayment = this.paymentsRepository.save(payments);

            Map<String, Object> response = new HashMap<>();
            response.put("payment", "Payment method created successfully!");
            response.put("paymentId", savedPayment.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public void validIfUserExistingByUserId(long userId, PaymentsDTO paymentsDTO) {
        Optional<User> existingUser = this.userRepository.findById(paymentsDTO.getUser().getId());
        if (existingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public void createPaymentIntent(
            long paymentId,
            long amount,
            String currency
    ) {
        try {
            Payments payment = validatesIfThereIsPaymentById(paymentId, amount, currency);
            PaymentIntent paymentIntent = createStripPaymentIntent(amount, currency, payment.getPaymentMethodId());

            updatePaymentInDataBase(payment, paymentIntent);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void messagePaymentApproved(String to) {
        String subject = "Payment method approved successfully!";
        String body = "Your payment method has been approved!";

        this.emailService.sendEmail(to, subject, body);
    }

    public PaymentIntent createStripPaymentIntent(
            long amount,
            String currency,
            String paymentMethodId
    ) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
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

    public Payments validatesIfThereIsPaymentById(long paymentId, long amount, String currency) {
        Optional<Payments> existingPayment = this.paymentsRepository.findById(paymentId);
        if (existingPayment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        if (amount <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid amount");
        }

        if (currency == null || currency.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency is required");
        }

        return existingPayment.get();
    }

    public void updatePaymentInDataBase(
            Payments payment,
            PaymentIntent paymentIntent
    ) {

        PaymentsDTO paymentsDTO = new PaymentsDTO();
        paymentsDTO.setPaymentStatus(paymentIntent.getStatus());
        paymentsDTO.setPaymentIntentId(paymentIntent.getId());
        paymentsDTO.setPaymentToken(paymentIntent.getClientSecret());
        paymentsDTO.setAmount(payment.getAmount());
        paymentsDTO.setCurrency(payment.getCurrency());

        Utils.copyNonNullProperties(paymentsDTO, payment);
        this.paymentsRepository.save(payment);
    }

    private Map<String, Object> buildResponseRequest(PaymentIntent paymentIntent) {
        Map<String, Object> response = new HashMap<>();
        if (!paymentIntent.getStatus().equals("succeeded")) {
            response.put("Status paymentIntent", paymentIntent.getStatus());
            response.put("message", "Payment failed");

            return response;
        }

        response.put("Status paymentIntent", paymentIntent.getStatus());
        response.put("message", "The payment has processed successfully!");

        return response;
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
