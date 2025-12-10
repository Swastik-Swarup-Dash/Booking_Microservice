package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.model.Booking;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {

    public void publishBookingCreated(Booking booking) {
        // Publish booking created event
    }

    public void publishBookingConfirmed(Booking booking) {
        // Publish booking confirmed event
    }

    public void publishBookingExpired(Booking booking) {
        // Publish booking expired event
    }
}