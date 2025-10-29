package com.seroter.MicroserviceBooking_app.repository.jpa;

import com.seroter.MicroserviceBooking_app.model.Booking;
import com.seroter.MicroserviceBooking_app.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByShowId(Long showId);
    List<Booking> findByStatus(BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.expirationTime < :now AND b.status = 'PENDING'")
    List<Booking> findExpiredBookings(@Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.show.id = :showId AND b.status IN ('CONFIRMED', 'PENDING')")
    Long countBookedSeats(@Param("showId") Long showId);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    List<Booking> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
}