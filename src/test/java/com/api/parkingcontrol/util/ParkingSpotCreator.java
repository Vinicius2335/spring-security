package com.api.parkingcontrol.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.parkingcontrol.api.representation.model.ParkingSpotPostDto;
import com.api.parkingcontrol.api.representation.model.ParkingSpotPutDto;
import com.api.parkingcontrol.domain.models.ParkingSpotModel;

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
	
	// Post
	public static ParkingSpotPostDto mockParkingSpotPostDto() {
		return ParkingSpotPostDto.builder()
				.parkingSpotNumber("123")
				.licensePlateCar("456")
				.brandCar("789")
				.modelCar("00")
				.colorCar("#FFFFFF")
				.responsibleName("Vinicius")
				.apartment("702")
				.block("A").build();
	}
	
	public static ParkingSpotPostDto mockInvalidParkingSpotPostDto() {
		return ParkingSpotPostDto.builder()
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
	
	// Put
	public static ParkingSpotPutDto mockParkingSpotPutDto() {
		return ParkingSpotPutDto.builder()
				.parkingSpotNumber("123")
				.licensePlateCar("456")
				.brandCar("789")
				.modelCar("00")
				.colorCar("#FFFFFF")
				.responsibleName("Vinicius")
				.apartment("702")
				.block("A").build();
	}
	
	public static ParkingSpotPutDto mockParkingSpotDtoToUpdated() {
		return ParkingSpotPutDto.builder()
				.parkingSpotNumber("123")
				.licensePlateCar("456")
				.brandCar("789")
				.modelCar("00")
				.colorCar("#FFFFFF")
				.responsibleName("ZORO")
				.apartment("702")
				.block("A").build();
	}
	
	public static ParkingSpotPutDto mockInvalidParkingSpotPutDto() {
		return ParkingSpotPutDto.builder()
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
