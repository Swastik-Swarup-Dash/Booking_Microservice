package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.dto.*;
import com.seroter.MicroserviceBooking_app.model.*;
import com.seroter.MicroserviceBooking_app.repository.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EnhancedBookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SeatManagementService seatService;
    @Autowired private PaymentService paymentService;
    @Autowired private EventPublisherService eventPublisher;

    public BookingResponse processCompleteBooking(BookingRequest request) {
        // Step 1: Validate and lock seats
        validateBookingRequest(request);
        SeatLock seatLock = SeatManagementService.lockSeats(request.getShowId(), request.getSeatNumbers());

        if (!seatLock.isSuccessful()) {
            throw new RuntimeException("Seats unavailable: " + seatLock.getMessage());
        }

        try {
            // Step 2: Create pending booking
            Booking booking = createPendingBooking(request);

            // Step 3: Initiate payment
            Payment payment = paymentService.initiatePayment(booking, PaymentMethod.CREDIT_CARD);

            // Step 4: Return response with payment details
            BookingResponse response = mapToResponse(booking);
            response.setPaymentIntentId(payment.getPaymentIntentId());

            eventPublisher.publishBookingCreated(booking);
            return response;

        } catch (Exception e) {
            seatService.releaseSeatLock(request.getShowId(), request.getSeatNumbers(), seatLock.getLockId());
            throw new RuntimeException("Booking failed: " + e.getMessage());
        }
    }

    public BookingResponse confirmBookingWithPayment(Long bookingId, String paymentIntentId) {
        Booking booking = getBookingById(bookingId);

        // Validate booking can be confirmed
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking cannot be confirmed");
        }

        if (booking.getExpirationTime().isBefore(LocalDateTime.now())) {
            expireBooking(booking);
            throw new RuntimeException("Booking expired");
        }

        // Process payment confirmation
        Payment payment = paymentService.confirmPayment(paymentIntentId);

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            // Confirm booking
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            // Permanently allocate seats
            SeatManagementService.confirmSeatBooking(booking.getShow().getId(), booking.getSeatNumbers());

            eventPublisher.publishBookingConfirmed(booking);
            return mapToResponse(booking);
        } else {
            throw new RuntimeException("Payment confirmation failed");
        }
    }

    public SeatAvailabilityResponse getRealTimeSeatAvailability(Long showId) {
        SeatAvailabilityResponse response = new SeatAvailabilityResponse();
        response.setShowId(showId);

        // Get available seats from Redis
        List<String> availableSeats = seatService.getAvailableSeats(showId)
                .stream().map(Object::toString).toList();

        // Get locked seats
        List<String> lockedSeats = seatService.getLockedSeats(showId);

        // Get permanently booked seats
        List<String> bookedSeats = getBookedSeatsForShow(showId);

        response.setAvailableSeats(availableSeats);
        response.setLockedSeats(lockedSeats);
        response.setBookedSeats(bookedSeats);
        response.setTotalSeats(100);

        return response;
    }

    private Booking createPendingBooking(BookingRequest request) {
        Booking booking = new Booking();
        booking.setUser(getUserById(request.getUserId()));
        booking.setShow(createDummyShow(request.getShowId()));
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setNumberOfSeats(request.getSeatNumbers().size());
        booking.setTotalPrice(calculatePrice(request.getSeatNumbers().size()));
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    private void validateBookingRequest(BookingRequest request) {
        if (request.getSeatNumbers().size() > 10) {
            throw new RuntimeException("Maximum 10 seats per booking");
        }

        if (!userRepository.existsById(request.getUserId())) {
            throw new RuntimeException("User not found");
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Show createDummyShow(Long showId) {
        Show show = new Show();
        show.setId(showId);
        show.setShowTime(LocalDateTime.now().plusHours(2));
        return show;
    }

    private BigDecimal calculatePrice(int seats) {
        return BigDecimal.valueOf(seats * 300); // ₹300 per seat
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    private void expireBooking(Booking booking) {
        booking.setStatus(BookingStatus.EXPIRED);
        bookingRepository.save(booking);
        SeatManagementService.releaseSeatLock(booking.getShow().getId(), booking.getSeatNumbers(), "expired");
        eventPublisher.publishBookingExpired(booking);
    }

    private List<String> getBookedSeatsForShow(Long showId) {
        return bookingRepository.findByShowId(showId).stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .flatMap(b -> b.getSeatNumbers().stream())
                .toList();
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setUserId(booking.getUser().getId());
        response.setShowId(booking.getShow().getId());
        response.setSeatNumbers(booking.getSeatNumbers());
        response.setNumberOfSeats(booking.getNumberOfSeats());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());
        response.setBookingTime(booking.getBookingTime());
        response.setExpirationTime(booking.getExpirationTime());
        return response;
    }
}
