package com.seroter.MicroserviceBooking_app.controller;

import com.seroter.MicroserviceBooking_app.model.Ticket;
import com.seroter.MicroserviceBooking_app.service.TicketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;

    public TicketController (TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @PostMapping
    public Ticket bookTicket(@RequestBody Ticket ticket) {
        return ticketRepository.save(ticket);
    }
}
