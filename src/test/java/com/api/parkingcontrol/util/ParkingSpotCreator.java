package com.api.parkingcontrol.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
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
	
	public static ParkingSpotDto mockParkingSpotDto() {
		return ParkingSpotDto.builder()
				.parkingSpotNumber("456")
				.licensePlateCar("789")
				.brandCar("012")
				.modelCar("01")
				.colorCar("#FFF000")
				.registrationDate(LocalDateTime.now())
				.responsibleName("Luffy")
				.apartment("303")
				.block("C").build();
	}
	
	public static ParkingSpotDto mockInvalidParkingSpotDto() {
		return ParkingSpotDto.builder()
				.parkingSpotNumber(null)
				.licensePlateCar(null)
				.brandCar(null)
				.modelCar("01")
				.colorCar("#FFF000")
				.registrationDate(LocalDateTime.now())
				.responsibleName("Luffy")
				.apartment("303")
				.block("C").build();
	}
}
