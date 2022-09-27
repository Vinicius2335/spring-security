package com.api.parkingcontrol.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parking-spot")
@CrossOrigin(origins = "*", maxAge = 3600) // cors, permite qualquer acesso à API
@RequiredArgsConstructor
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        parkingSpotDto.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(parkingSpotService.save(parkingSpotDto));
    }

    // Paginação
    // TODO: TESTAR PAGINAÇAO DPS
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpot(@PageableDefault(page = 0, size = 10, sort = "idParking", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(parkingSpotService.findAll(pageable));
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotModel> getOneParkingSpot(@PathVariable UUID id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(parkingSpotService.findByIdOrThrowsException(id));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable UUID id){
        parkingSpotService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Parking Spot deleted successfully");
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        parkingSpotService.updated(id, parkingSpotDto);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Parking Spot deleted successfully");
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