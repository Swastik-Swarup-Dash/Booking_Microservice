package com.seroter.MicroserviceBooking_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.seroter.MicroserviceBooking_app.service")
@EntityScan(basePackages = "com.seroter.MicroserviceBooking_app.model")

@SpringBootApplication
public class MicroserviceBookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceBookingAppApplication.class, args);
	}

}
