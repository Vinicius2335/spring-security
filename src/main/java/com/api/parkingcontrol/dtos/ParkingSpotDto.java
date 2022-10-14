package com.api.parkingcontrol.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.api.parkingcontrol.annotations.ApartmentValidation;
import com.api.parkingcontrol.annotations.LicensePlateCarValidation;
import com.api.parkingcontrol.annotations.ParkingSpotNumberValidation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ParkingSpotDto {
	
	@NotBlank(message = "The parkingSpotNumber field cannot be blank")
	@Size(max = 10, message = "The parkingSpotNumber field should not be greater than 10 characters")
	@ParkingSpotNumberValidation
	private String parkingSpotNumber;
	
	@NotBlank(message = "The licensePlateCar field cannot be blank")
	@Size(max = 7, message = "The licensePlateCar field should not be greater than 7 characters")
	@LicensePlateCarValidation
	private String licensePlateCar;
	
	@NotBlank(message = "The brandCar field cannot be blank")
	@Size(max = 70, message = "The brandCar field should not be greater than 70 characters")
	private String brandCar;
	
	@NotBlank(message = "The modelCar field cannot be blank")
	@Size(max = 70, message = "The modelCar field should not be greater than 70 characters")
	private String modelCar;
	
	@NotBlank(message = "The colorCar field cannot be blank")
	@Size(max = 70, message = "The colorCar field should not be greater than 70 characters")
	private String colorCar;
	
	private LocalDateTime registrationDate;
	
	@NotBlank(message = "The responsibleName field cannot be blank")
	@Size(max = 130, message = "The responsibleName field should not be greater than 130 characters")
	private String responsibleName;
	
	@NotBlank(message = "The apartment field cannot be blank")
	@Size(max = 30, message = "The apartment field should not be greater than 30 characters")
	@ApartmentValidation
	private String apartment;
	
	@NotBlank(message = "The block field cannot be blank")
	@Size(max = 30, message = "The block field should not be greater than 30 characters")
	private String block;

}
