package com.seroter.MicroserviceBooking_app.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SeatLock {
    private String lockId;
    private Long showId;
    private List<String> seatNumbers;
    private boolean successful;
    private String message;
    private LocalDateTime expirationTime;
}