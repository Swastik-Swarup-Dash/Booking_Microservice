package com.seroter.MicroserviceBooking_app.service;


import com.seroter.MicroserviceBooking_app.dto.BookingRequest;
import com.seroter.MicroserviceBooking_app.dto.BookingResponse;
import com.seroter.MicroserviceBooking_app.model.*;
import com.seroter.MicroserviceBooking_app.repository.jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatManagementService seatManagementService;

    @Transactional
    public BookingResponse createBooking(BookingRequest request){
        SeatLock seatlock = seatManagementService.lockSeats(request.getShowId(),request.getSeatNumbers());
        if(!seatlock.isSuccessful()){
            throw new RuntimeException("Seats Booking Failed" + seatlock.getMessage());
        }
        Booking booking = new Booking();
        booking.setUser(userRepository.findById(request.getUserId()).orElseThrow());
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setNumberOfSeats(request.getSeatNumbers().size());
        booking.setTotalPrice(BigDecimal.valueOf(request.getSeatNumbers().size() * 100));
        
        booking = bookingRepository.save(booking);
        return  mapToResponse(booking);
    }

    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(BookingStatus.CONFIRMED);
        seatManagementService.confirmSeatBooking(booking.getShow().getId(), booking.getSeatNumbers());
        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setStatus(BookingStatus.CANCELLED);
        // Release seat locks - you'll need lockId from booking
        bookingRepository.save(booking);
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
