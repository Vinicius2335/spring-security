package com.api.parkingcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class ParkingControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingControlApplication.class, args);
		//System.out.println("Password: " + new BCryptPasswordEncoder().encode("senha123"));
		//System.out.println("UUID: " + UUID.randomUUID());
	}

}