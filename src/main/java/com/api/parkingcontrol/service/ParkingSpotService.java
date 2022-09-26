package com.api.parkingcontrol.service;


import com.api.parkingcontrol.dtos.ParkingSpotDto;
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
	public ParkingSpotModel save(ParkingSpotDto parkingSpotDto) {
		return parkingSpotRepository.save(ParkingSpotMapper.INSTANCE.toParkingSpotModel(parkingSpotDto));
	}

	public boolean existsByLicensePlateCar(String licensePlateCar) {
		return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
	}

	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public boolean existsByApartamentAndBlock(String apartament, String block) {
		return parkingSpotRepository.existsByApartamentAndBlock(apartament, block);
	}

	public boolean existsApartament(String apartament){
		return parkingSpotRepository.existsByApartament(apartament);
	}

	//public boolean existsBlock(String block){
	//	return parkingSpotRepository.existsByBlock(block);
	//}

	public Page<ParkingSpotModel> findAll(Pageable pageable){
		return parkingSpotRepository.findAll(pageable);
	}

	public ParkingSpotModel findByIdOrThrowsException(UUID id){
		return parkingSpotRepository.findById(id)
				.orElseThrow(() -> new ParkingSpotNotFoundException("Id não encontrado"));
	}

	@Transactional
	// TODO: REVER A AULA SOBRE Transactional do DevDojo
	public void delete(UUID id){
		ParkingSpotModel parkingSpotFound = findByIdOrThrowsException(id);
		parkingSpotRepository.delete(parkingSpotFound);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updated(UUID id, ParkingSpotDto parkingSpotDto){
		ParkingSpotModel parkingSpotFound = findByIdOrThrowsException(id);
		ParkingSpotModel parkingSpotUpdated = ParkingSpotMapper.INSTANCE.toParkingSpotModel(parkingSpotDto);

		parkingSpotUpdated.setIdParking(parkingSpotFound.getIdParking());
		parkingSpotUpdated.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		parkingSpotRepository.save(parkingSpotUpdated);
	}
}
