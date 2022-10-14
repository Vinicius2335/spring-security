package com.api.parkingcontrol.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.util.ParkingSpotCreator;

@DataJpaTest
@DisplayName("Test for parking spot repository")
class ParkingSpotRepositoryTest {
	@Autowired
	private ParkingSpotRepository parkingSpotRepository;
	
	private ParkingSpotModel parkingSpotToSave;
	
	@BeforeEach
	void setUp() throws Exception {
		parkingSpotToSave = ParkingSpotCreator.mockParkingSpot();
	}
	
	private ParkingSpotModel savingParkingSpot() {
		ParkingSpotModel parkingSpot = parkingSpotRepository.save(parkingSpotToSave);
		return parkingSpot;
	}
	
	@Test
	@DisplayName("save insert parking spot when successful")
	public void parkingSpot_InsertParkingSpot_WhenSuccessful() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		if (parkingSpot != null) {
			parkingSpotToSave.setIdParking(parkingSpot.getIdParking());
		}
		
		assertAll(
				() -> assertNotNull(parkingSpot),
				() -> assertEquals(parkingSpotToSave, parkingSpot)
		);
	}
	
	@Test
	@DisplayName("existsByLicensePlateCar return true when licensePlateCar exists in database")
	public void existsByLicensePlateCar_ReturnTrue_WhenParkingControlLicensePlateCarExists() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		boolean existsByLicensePlateCar = parkingSpotRepository.existsByLicensePlateCar(parkingSpot
				.getLicensePlateCar());
		
		assertTrue(existsByLicensePlateCar);
	}

	
	@Test
	@DisplayName("existsByLicensePlateCar return false when licensePlateCar dont exists in database")
	public void existsByLicensePlateCar_ReturnFalse_WhenParkingControlLicensePlateCarDontExists() {
		boolean existsByLicensePlateCar = parkingSpotRepository.existsByLicensePlateCar("231");
		
		assertFalse(existsByLicensePlateCar);
	}
	
	@Test
	@DisplayName("existsByParkingSpotNumber return true when ParkingSpotNumber exists in database")
	public void existsByParkingSpotNumber_ReturnTrue_WhenParkingSpotNumberExists() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		boolean existsByParkingSpotNumber = parkingSpotRepository.existsByParkingSpotNumber(parkingSpot
				.getParkingSpotNumber());
		
		assertTrue(existsByParkingSpotNumber);
	}
	
	@Test
	@DisplayName("existsByParkingSpotNumber return false when ParkingSpotNumber dont exists in database")
	public void existsByParkingSpotNumber_ReturnFalse_WhenParkingSpotNumberDontExists() {
		boolean existsByParkingSpotNumber = parkingSpotRepository.existsByParkingSpotNumber("984");
		
		assertFalse(existsByParkingSpotNumber);
	}
	
	@Test
	@DisplayName("existsByApartmentAndBlock return true when ApartmentAndBlock exists in database")
	public void existsByApartmentAndBlock_ReturnTrue_WhenApartamentAndBlockExists() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		boolean existsByApartmentAndBlock = parkingSpotRepository.existsByApartmentAndBlock(parkingSpot
				.getApartment(), parkingSpot.getBlock());
		
		assertTrue(existsByApartmentAndBlock);
	}
	
	@Test
	@DisplayName("existsByApartmentAndBlock return false when ApartmentAndBlock dont exists in database")
	public void existsByApartmentAndBlock_ReturnFalse_WhenApartamentAndBlockDontExists() {
		boolean existsByApartmentAndBlock = parkingSpotRepository.existsByApartmentAndBlock("190", "Z");
		
		assertFalse(existsByApartmentAndBlock);
	}
	
	@Test
	@DisplayName("existsApartament return true when Apartament exists in database")
	public void existsApartament_ReturnTrue_WhenApartamentExists() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		boolean existsByApartment = parkingSpotRepository.existsByApartment(parkingSpot.getApartment());
		
		assertTrue(existsByApartment);
	}
	
	@Test
	@DisplayName("existsApartament return false when Apartament exists in database")
	public void existsApartament_ReturnFalse_WhenApartamentDontExists() {
		boolean existsByApartment = parkingSpotRepository.existsByApartment("C8A");
		
		assertFalse(existsByApartment);
	}
	
	@Test
	@DisplayName("findAll return page of parking spot when successful")
	public void findAll_ReturnPageParkingSpot_WhenSuccessful() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		parkingSpotRepository.save(parkingSpot);
		Page<ParkingSpotModel> findAllPage = parkingSpotRepository.findAll(Pageable.ofSize(1));
		
		assertAll(
				() -> assertNotNull(findAllPage),
				() -> assertFalse(findAllPage.isEmpty()),
				() -> assertEquals(1, findAllPage.getContent().size())
		);
	}
	
	@Test
	@DisplayName("findAll return empty page of parking spot when dont have parking spot in database")
	public void findAll_ReturnEmptyPageParkingSpot_WhenDontHaveParkingSpotInDatabase() {
		Page<ParkingSpotModel> findAllPage = parkingSpotRepository.findAll(Pageable.ofSize(1));
		
		assertAll(
				() -> assertNotNull(findAllPage),
				() -> assertTrue(findAllPage.isEmpty())
		);
	}
	
	@Test
	@DisplayName("findById return optional parkingSpot when successful")
	public void findById_ReturnOptionalParkingSpot_WhenSuccessful() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		Optional<ParkingSpotModel> findById = parkingSpotRepository.findById(parkingSpot.getIdParking());
		
		assertAll(
				() -> assertNotNull(findById),
				() -> assertFalse(findById.isEmpty()),
				() -> assertTrue(findById.isPresent()),
				() -> assertEquals(parkingSpot, findById.get())
		);
	}
	
	@Test
	@DisplayName("findById return empty Optional parkingSpot when parking spot not found")
	public void findById_ReturnEmptyOptionalParkingSpot_WhenParkingSpotNotFound() {
		Optional<ParkingSpotModel> findById = parkingSpotRepository.findById(UUID.randomUUID());
		
		assertAll(
				() -> assertNotNull(findById),
				() -> assertTrue(findById.isEmpty()),
				() -> assertFalse(findById.isPresent())
		);
	}
	
	@Test
	@DisplayName("delete remove parking spot when successful")
	public void delete_RemoveParkingSpot_WhenSuccessful() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		assertDoesNotThrow(() -> parkingSpotRepository.delete(parkingSpot));
	}
	
	@Test
	@DisplayName("delete Throw InvalidDataAccessApiUsageException when parkingSpot is null")
	public void delete_ThrowInvalidDataAccessApiUsageException_WhenParkingSpotIsNull() {
		ParkingSpotModel parkingSpot = null;
		
		assertThrows(InvalidDataAccessApiUsageException.class, ()-> parkingSpotRepository.delete(parkingSpot));
	}
	
	@Test
	@DisplayName("updated replace parking spot when successful")
	public void updated_ReplaceParkingSpot_WhenSuccessful() {
		ParkingSpotModel parkingSpot = savingParkingSpot();
		
		ParkingSpotModel parkingSpotToUpdate = ParkingSpotCreator.mockParkingSpot();
		
		parkingSpotToUpdate.setIdParking(parkingSpot.getIdParking());
		parkingSpotToUpdate.setApartment("0001");
		
		assertDoesNotThrow(() -> parkingSpotRepository.save(parkingSpotToUpdate));
	}
}
