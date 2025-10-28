package com.seroter.MicroserviceBooking_app.repository.jpa;

import com.seroter.MicroserviceBooking_app.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
