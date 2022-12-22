package com.api.parkingcontrol.domain.service;

import com.api.parkingcontrol.api.representation.model.ParkingSpotPostDto;
import com.api.parkingcontrol.api.representation.model.ParkingSpotPutDto;
import com.api.parkingcontrol.domain.models.ParkingSpotModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ParkingSpotService {
    ParkingSpotModel save(ParkingSpotPostDto parkingSpotDto);

    boolean existsByLicensePlateCar(String licensePlateCar);

    boolean existsByParkingSpotNumber(String parkingSpotNumber);

    boolean existsByApartmentAndBlock(String apartment, String block);

    boolean existsApartament(String apartament);

    Page<ParkingSpotModel> findAll(Pageable pageable);

    ParkingSpotModel findByIdOrThrowsParkingSpotNotFoundException(UUID id);

    void delete(UUID id);

    void updated(UUID id, ParkingSpotPutDto parkingSpotPutDto);
}
