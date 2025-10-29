package com.seroter.MicroserviceBooking_app.service;

import com.seroter.MicroserviceBooking_app.model.SeatLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SeatManagementService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String SEAT_LOCK_PREFIX = "seat_lock:";
    private static final String SEAT_AVAILABILITY_PREFIX = "seat_availability:";
    private static final int LOCK_DURATION_MINUTES = 15;
    
    public SeatLock lockSeats(Long showId, List<String> seatNumbers) {
        String availabilityKey = SEAT_AVAILABILITY_PREFIX + showId;
        
        // Check if seats are available
        Set<Object> availableSeats = redisTemplate.opsForSet().members(availabilityKey);
        List<String> availableSeatStrings = availableSeats.stream()
            .map(Object::toString)
            .collect(Collectors.toList());
        
        if (!availableSeatStrings.containsAll(seatNumbers)) {
            return SeatLock.failed("Some seats are not available");
        }
        
        // Try to lock each seat
        String lockId = UUID.randomUUID().toString();
        for (String seatNumber : seatNumbers) {
            String seatLockKey = SEAT_LOCK_PREFIX + showId + ":" + seatNumber;
            Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(seatLockKey, lockId, Duration.ofMinutes(LOCK_DURATION_MINUTES));
            
            if (!locked) {
                // Rollback previous locks
                releaseSeatLock(showId, seatNumbers, lockId);
                return SeatLock.failed("Seat " + seatNumber + " is already locked");
            }
        }
        
        // Remove from available seats temporarily
        redisTemplate.opsForSet().remove(availabilityKey, seatNumbers.toArray());
        
        return SeatLock.success(showId, seatNumbers, lockId);
    }
    
    public void releaseSeatLock(Long showId, List<String> seatNumbers, String lockId) {
        String availabilityKey = SEAT_AVAILABILITY_PREFIX + showId;
        
        for (String seatNumber : seatNumbers) {
            String seatLockKey = SEAT_LOCK_PREFIX + showId + ":" + seatNumber;
            String currentLockId = (String) redisTemplate.opsForValue().get(seatLockKey);
            
            // Only release if we own the lock
            if (lockId.equals(currentLockId)) {
                redisTemplate.delete(seatLockKey);
            }
        }
        
        // Add back to available seats
        redisTemplate.opsForSet().add(availabilityKey, seatNumbers.toArray());
    }
    
    public void confirmSeatBooking(Long showId, List<String> seatNumbers) {
        // Remove seats from availability permanently
        String availabilityKey = SEAT_AVAILABILITY_PREFIX + showId;
        redisTemplate.opsForSet().remove(availabilityKey, seatNumbers.toArray());
        
        // Remove locks
        for (String seatNumber : seatNumbers) {
            String seatLockKey = SEAT_LOCK_PREFIX + showId + ":" + seatNumber;
            redisTemplate.delete(seatLockKey);
        }
    }
    
    public Set<Object> getAvailableSeats(Long showId) {
        String availabilityKey = SEAT_AVAILABILITY_PREFIX + showId;
        return redisTemplate.opsForSet().members(availabilityKey);
    }
    
    public void initializeSeatsForShow(Long showId, List<String> allSeats) {
        String availabilityKey = SEAT_AVAILABILITY_PREFIX + showId;
        redisTemplate.opsForSet().add(availabilityKey, allSeats.toArray());
    }
}