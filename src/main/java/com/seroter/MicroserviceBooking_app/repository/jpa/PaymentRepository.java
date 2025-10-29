package com.seroter.MicroserviceBooking_app.repository.jpa;

import com.seroter.MicroserviceBooking_app.model.Payment;
import com.seroter.MicroserviceBooking_app.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByStatus(PaymentStatus status);
}