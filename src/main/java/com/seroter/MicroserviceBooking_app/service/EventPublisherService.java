package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.model.Booking;
import com.seroter.MicroserviceBooking_app.model.Payment;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventPublisherService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public EventPublisherService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public void publishBookingCreated(Booking booking) {
        log.info("Publishing booking created event for booking: {}", booking.getId());
        // In real implementation, this would publish to Kafka/EventBridge
        // eventPublisher.publishEvent(new BookingCreatedEvent(booking));
    }
    
    public void publishBookingConfirmed(Booking booking) {
        log.info("Publishing booking confirmed event for booking: {}", booking.getId());
        // eventPublisher.publishEvent(new BookingConfirmedEvent(booking));
    }
    
    public void publishBookingCancelled(Booking booking) {
        log.info("Publishing booking cancelled event for booking: {}", booking.getId());
        // eventPublisher.publishEvent(new BookingCancelledEvent(booking));
    }
    
    public void publishBookingExpired(Booking booking) {
        log.info("Publishing booking expired event for booking: {}", booking.getId());
        // eventPublisher.publishEvent(new BookingExpiredEvent(booking));
    }
    
    public void publishPaymentInitiated(Payment payment) {
        log.info("Publishing payment initiated event for payment: {}", payment.getId());
        // eventPublisher.publishEvent(new PaymentInitiatedEvent(payment));
    }
    
    public void publishPaymentCompleted(Payment payment) {
        log.info("Publishing payment completed event for payment: {}", payment.getId());
        // eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
    }
    
    public void publishPaymentFailed(Long bookingId, String reason) {
        log.info("Publishing payment failed event for booking: {}, reason: {}", bookingId, reason);
        // eventPublisher.publishEvent(new PaymentFailedEvent(bookingId, reason));
    }
    
    public void publishPaymentRefunded(Payment payment) {
        log.info("Publishing payment refunded event for payment: {}", payment.getId());
        // eventPublisher.publishEvent(new PaymentRefundedEvent(payment));
    }
}