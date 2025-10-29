package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.model.*;
import com.seroter.MicroserviceBooking_app.repository.jpa.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private EventPublisherService eventPublisher;
    
    public Payment initiatePayment(Booking booking, PaymentMethod method) {
        try {
            // Create payment intent (simulated for now)
            String paymentIntentId = "pi_" + UUID.randomUUID().toString();
            
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setPaymentIntentId(paymentIntentId);
            payment.setAmount(booking.getTotalPrice());
            payment.setMethod(method);
            payment.setStatus(PaymentStatus.PENDING);
            
            Payment savedPayment = paymentRepository.save(payment);
            
            // Publish payment initiated event
            eventPublisher.publishPaymentInitiated(savedPayment);
            
            return savedPayment;
            
        } catch (Exception e) {
            eventPublisher.publishPaymentFailed(booking.getId(), e.getMessage());
            throw new RuntimeException("Payment initiation failed", e);
        }
    }
    
    public Payment processPayment(String paymentIntentId, Map<String, Object> paymentData) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        try {
            // Simulate payment processing
            boolean paymentSuccessful = simulatePaymentGateway(payment, paymentData);
            
            if (paymentSuccessful) {
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setTransactionId("txn_" + UUID.randomUUID().toString());
                payment.setGatewayResponse("Payment successful");
                
                eventPublisher.publishPaymentCompleted(payment);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setGatewayResponse("Payment failed");
                
                eventPublisher.publishPaymentFailed(payment.getBooking().getId(), "Payment processing failed");
            }
            
            return paymentRepository.save(payment);
            
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setGatewayResponse("Error: " + e.getMessage());
            paymentRepository.save(payment);
            
            eventPublisher.publishPaymentFailed(payment.getBooking().getId(), e.getMessage());
            throw new RuntimeException("Payment processing failed", e);
        }
    }
    
    private boolean simulatePaymentGateway(Payment payment, Map<String, Object> paymentData) {
        // Simulate payment gateway call
        // In real implementation, this would call Stripe/Razorpay API
        return Math.random() > 0.1; // 90% success rate for simulation
    }
    
    public Payment refundPayment(Long paymentId, BigDecimal refundAmount) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Cannot refund non-completed payment");
        }
        
        // Process refund (simulated)
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setGatewayResponse("Refund processed: " + refundAmount);
        
        Payment refundedPayment = paymentRepository.save(payment);
        eventPublisher.publishPaymentRefunded(refundedPayment);
        
        return refundedPayment;
    }
}