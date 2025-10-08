package com.seroter.MicroserviceBooking_app.controller;

import com.seroter.MicroserviceBooking_app.model.Ticket;
import com.seroter.MicroserviceBooking_app.service.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;

    public TicketController (TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketebyId(@PathVariable Long Id){
        Optional<Ticket> ticket = ticketRepository.findById(Id);
        return ticket.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long Id , @RequestBody Ticket ticketdetails){
        Optional<Ticket> tickets= ticketRepository.findById(Id);

        if(tickets.isPresent()){
            Ticket ticket = tickets.get();
            ticket.setEventName(ticket.getEventName());
            ticket.setUserName(ticket.getUserName());
            ticket.setSeatNumber(ticket.getSeatNumber());
            Ticket updatedTicket = ticketRepository.save(ticket);
            return ResponseEntity.ok(updatedTicket);
        }else{
            return  ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
    if (ticketRepository.existsById(id)) {
     ticketRepository.deleteById(id);
      return ResponseEntity.noContent().build();
  } else {
      return ResponseEntity.notFound().build();
    }
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
