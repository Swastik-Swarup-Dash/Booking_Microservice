package com.seroter.MicroserviceBooking_app.controller;

import com.seroter.MicroserviceBooking_app.service.SeatManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/seats")
public class Seatcontroller {
    @Autowired
    private SeatManagementService seatManagementService;

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeSeats(@RequestBody Map<String, Object> request) {
        Long showId = Long.valueOf(request.get("showId").toString());
        List<String> seats = (List<String>) request.get("seats");

        seatManagementService.initializeSeatsForShow(showId, seats);
        return ResponseEntity.ok("Seats initialized for show " + showId);
    }

    @GetMapping("/{showId}/available")
    public ResponseEntity<Set<Object>> getAvailableSeats(@PathVariable Long showId) {
        Set<Object> availableSeats = seatManagementService.getAvailableSeats(showId);
        return ResponseEntity.ok(availableSeats);
    }
}
