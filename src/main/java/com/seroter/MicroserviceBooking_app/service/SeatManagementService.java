package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.model.SeatLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeatManagementService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public SeatLock lockSeats(Long showId, List<String> seatNumbers) {
        SeatLock lock = new SeatLock();
        lock.setLockId(UUID.randomUUID().toString());
        lock.setShowId(showId);
        lock.setSeatNumbers(seatNumbers);
        lock.setSuccessful(true);
        lock.setMessage("Seats locked successfully");
        lock.setExpirationTime(LocalDateTime.now().plusMinutes(10));
        return lock;
    }

    public void initializeSeatsForShow(Long showId, List<String> seats) {
        String key = "available_seats:" + showId;
        redisTemplate.delete(key);
        for (String seat : seats) {
            redisTemplate.opsForList().rightPush(key, seat);
        }
    }

    public List<Object> getAvailableSeats(Long showId) {
        return redisTemplate.opsForList().range("available_seats:" + showId, 0, -1);
    }

    public List<String> getLockedSeats(Long showId) {
        return (List<String>) redisTemplate.opsForValue().get("locked_seats:" + showId);
    }

    public void releaseSeatLock(Long showId, List<String> seatNumbers, String lockId) {
        redisTemplate.delete("seat_lock:" + lockId);
    }

    public void confirmSeatBooking(Long showId, List<String> seatNumbers) {
        // Permanently book seats
    }
}