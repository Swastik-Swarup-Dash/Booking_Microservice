package com.seroter.MicroserviceBooking_app.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SeatLock {
    private Long showId;
    private List<String> seatNumbers;
    private String lockId;
    private LocalDateTime expiryTime;
    private boolean successful;
    private String message;
    
    public static SeatLock success(Long showId, List<String> seatNumbers, String lockId) {
        SeatLock lock = new SeatLock();
        lock.setShowId(showId);
        lock.setSeatNumbers(seatNumbers);
        lock.setLockId(lockId);
        lock.setExpiryTime(LocalDateTime.now().plusMinutes(15));
        lock.setSuccessful(true);
        lock.setMessage("Seats locked successfully");
        return lock;
    }
    
    public static SeatLock failed(String message) {
        SeatLock lock = new SeatLock();
        lock.setSuccessful(false);
        lock.setMessage(message);
        return lock;
    }
}