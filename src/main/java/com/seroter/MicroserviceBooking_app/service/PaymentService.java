package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.model.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    public Payment initiatePayment(Booking booking, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setPaymentIntentId(UUID.randomUUID().toString());
        payment.setAmount(booking.getTotalPrice());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(method);
        payment.setCreatedAt(LocalDateTime.now());
        return payment;
    }

    public Payment confirmPayment(String paymentIntentId) {
        Payment payment = new Payment();
        payment.setPaymentIntentId(paymentIntentId);
        payment.setStatus(PaymentStatus.COMPLETED);
        return payment;
    }
}