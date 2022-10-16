package com.api.parkingcontrol.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotPostDto;
import com.api.parkingcontrol.dtos.ParkingSpotPutDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parking-spot")
@CrossOrigin(origins = "*", maxAge = 3600) // cors, permite qualquer acesso à API
@RequiredArgsConstructor
public class ParkingSpotController {
	private final ParkingSpotService parkingSpotService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ParkingSpotModel> saveParkingSpot(@RequestBody @Valid ParkingSpotPostDto parkingSpotPostDto) {
		parkingSpotPostDto.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotPostDto));
	}

	// Paginação
	// TODO: TESTAR PAGINAÇAO DPS
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpot(
			@PageableDefault(page = 0, size = 10, sort = "idParking", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/{id}")
	public ResponseEntity<ParkingSpotModel> getOneParkingSpot(@PathVariable UUID id) {
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findByIdOrThrowsParkingSpotNotFoundException(id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable UUID id) {
		parkingSpotService.delete(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Parking Spot deleted successfully");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable UUID id,
			@RequestBody @Valid ParkingSpotPutDto parkingSpotPutDto) {
		parkingSpotService.updated(id, parkingSpotPutDto);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Parking Spot updated successfully");
	}
}

//if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
//    return ResponseEntity
//            .status(HttpStatus.CONFLICT)
//            .body("Conflict: License Plate Car is already in use!");
//}

//if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
//    return ResponseEntity
//            .status(HttpStatus.CONFLICT)
//            .body("Conflict: Parking Spot Number is already in use!");
//}

//if (parkingSpotService.existsByApartamentAndBlock(parkingSpotDto.getApartament(),
//        parkingSpotDto.getBlock())) {
//    return ResponseEntity
//            .status(HttpStatus.CONFLICT)
//            .body("Conflict: Parking Spot already in registered for this apartament/block!");
//}