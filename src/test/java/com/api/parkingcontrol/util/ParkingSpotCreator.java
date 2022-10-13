package com.api.parkingcontrol.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.parkingcontrol.models.ParkingSpotModel;

public class ParkingSpotCreator {
	public static ParkingSpotModel mockParkingSpot() {
		return ParkingSpotModel.builder()
				.idParking(UUID.randomUUID())
				.parkingSpotNumber("123")
				.licensePlateCar("456")
				.brandCar("789")
				.modelCar("00")
				.colorCar("#FFFFFF")
				.registrationDate(LocalDateTime.now())
				.responsibleName("Vinicius")
				.apartment("702")
				.block("A").build();
	}
}
