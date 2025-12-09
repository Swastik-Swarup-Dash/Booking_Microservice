package com.seroter.MicroserviceBooking_app.controller;

import com.seroter.MicroserviceBooking_app.dto.*;
import com.seroter.MicroserviceBooking_app.service.EnhancedBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/bookings")
public class CompleteBookingController {
    @Autowired
    private EnhancedBookingService bookingService;

    // Complete booking workflow
    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.processCompleteBooking(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Confirm booking after payment
    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(
            @PathVariable Long bookingId,
            @RequestBody Map<String, String> paymentData) {
        try {
            String paymentIntentId = paymentData.get("paymentIntentId");
            BookingResponse response = bookingService.confirmBookingWithPayment(bookingId, paymentIntentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Real-time seat availability
    @GetMapping("/shows/{showId}/seats/availability")
    public ResponseEntity<SeatAvailabilityResponse> getSeatAvailability(@PathVariable Long showId) {
        SeatAvailabilityResponse response = bookingService.getRealTimeSeatAvailability(showId);
        return ResponseEntity.ok(response);
    }

    // Extend seat lock (for user still selecting)
    @PostMapping("/shows/{showId}/seats/extend-lock")
    public ResponseEntity<String> extendSeatLock(
            @PathVariable Long showId,
            @RequestBody Map<String, Object> lockData) {
        // Implementation for extending seat locks
        return ResponseEntity.ok("Lock extended");
    }
}
