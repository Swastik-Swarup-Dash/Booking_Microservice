package com.seroter.MicroserviceBooking_app.dto;

import lombok.Data;
import java.util.List;

@Data
public class SeatAvailabilityResponse {
    private Long showId;
    private List<String> availableSeats;
    private List<String> lockedSeats;
    private List<String> bookedSeats;
    private int totalSeats;
}
