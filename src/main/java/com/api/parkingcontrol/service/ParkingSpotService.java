package com.api.parkingcontrol.service;


import com.api.parkingcontrol.dtos.ParkingSpotPostDto;
import com.api.parkingcontrol.dtos.ParkingSpotPutDto;
import com.api.parkingcontrol.exception.ParkingSpotNotFoundException;
import com.api.parkingcontrol.mapper.ParkingSpotMapper;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ParkingSpotService {
	private final ParkingSpotRepository parkingSpotRepository;
	
	@Transactional(rollbackFor = { Exception.class })
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

	public boolean existsApartament(String apartament){
		return parkingSpotRepository.existsByApartment(apartament);
	}

	//public boolean existsBlock(String block){
	//	return parkingSpotRepository.existsByBlock(block);
	//}

	public Page<ParkingSpotModel> findAll(Pageable pageable){
		return parkingSpotRepository.findAll(pageable);
	}

	public ParkingSpotModel findByIdOrThrowsParkingSpotNotFoundException(UUID id){
		return parkingSpotRepository.findById(id)
				.orElseThrow(() -> new ParkingSpotNotFoundException("Id n??o encontrado"));
	}

	@Transactional
	// TODO: REVER A AULA SOBRE Transactional do DevDojo
	public void delete(UUID id){
		ParkingSpotModel parkingSpotFound = findByIdOrThrowsParkingSpotNotFoundException(id);
		parkingSpotRepository.delete(parkingSpotFound);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updated(UUID id, ParkingSpotPutDto parkingSpotPutDto){
		ParkingSpotModel parkingSpotFound = findByIdOrThrowsParkingSpotNotFoundException(id);
		ParkingSpotModel parkingSpotUpdated = ParkingSpotMapper.INSTANCE.toParkingSpotModel(parkingSpotPutDto);

		parkingSpotUpdated.setIdParking(parkingSpotFound.getIdParking());
		parkingSpotUpdated.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		parkingSpotRepository.save(parkingSpotUpdated);
	}
}
