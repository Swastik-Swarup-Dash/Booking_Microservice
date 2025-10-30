package com.seroter.MicroserviceBooking_app.dto;
import com.seroter.MicroserviceBooking_app.model.BookingStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long showId;
    private List<String> seatNumbers;
    private int numberOfSeats;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private LocalDateTime expirationTime;
    private String paymentIntentId;
}
