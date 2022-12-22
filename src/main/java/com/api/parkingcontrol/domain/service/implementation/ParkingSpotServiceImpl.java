package com.api.parkingcontrol.domain.service.implementation;


import com.api.parkingcontrol.api.representation.model.ParkingSpotPostDto;
import com.api.parkingcontrol.api.representation.model.ParkingSpotPutDto;
import com.api.parkingcontrol.domain.exception.ParkingSpotNotFoundException;
import com.api.parkingcontrol.api.mapper.ParkingSpotMapper;
import com.api.parkingcontrol.domain.models.ParkingSpotModel;
import com.api.parkingcontrol.domain.repository.ParkingSpotRepository;
import com.api.parkingcontrol.domain.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RequiredArgsConstructor
@Primary
@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ParkingSpotModel save(ParkingSpotPostDto parkingSpotDto) {
        return parkingSpotRepository.save(ParkingSpotMapper.INSTANCE.toParkingSpotModel(parkingSpotDto));
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public boolean existsApartament(String apartament) {
        return parkingSpotRepository.existsByApartment(apartament);
    }

    //public boolean existsBlock(String block){
    //	return parkingSpotRepository.existsByBlock(block);
    //}

    public Page<ParkingSpotModel> findAll(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable);
    }

    public ParkingSpotModel findByIdOrThrowsParkingSpotNotFoundException(UUID id) {
        return parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException("Id não encontrado"));
    }

    @Transactional
    // TODO: REVER A AULA SOBRE Transactional do DevDojo
    public void delete(UUID id) {
        ParkingSpotModel parkingSpotFound = findByIdOrThrowsParkingSpotNotFoundException(id);
        parkingSpotRepository.delete(parkingSpotFound);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updated(UUID id, ParkingSpotPutDto parkingSpotPutDto) {
        ParkingSpotModel parkingSpotFound = findByIdOrThrowsParkingSpotNotFoundException(id);
        ParkingSpotModel parkingSpotUpdated = ParkingSpotMapper.INSTANCE.toParkingSpotModel(parkingSpotPutDto);

        parkingSpotUpdated.setIdParking(parkingSpotFound.getIdParking());
        parkingSpotUpdated.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parkingSpotRepository.save(parkingSpotUpdated);
    }
}
