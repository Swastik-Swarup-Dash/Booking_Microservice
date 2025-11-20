package com.seroter.MicroserviceBooking_app.repository.jpa;

import com.seroter.MicroserviceBooking_app.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
}
