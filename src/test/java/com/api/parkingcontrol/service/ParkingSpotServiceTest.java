package com.api.parkingcontrol.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.parkingcontrol.dtos.ParkingSpotPostDto;
import com.api.parkingcontrol.dtos.ParkingSpotPutDto;
import com.api.parkingcontrol.exception.ParkingSpotNotFoundException;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import com.api.parkingcontrol.util.ParkingSpotCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for Parking Spot Service")
class ParkingSpotServiceTest {
	
	@InjectMocks
	private ParkingSpotService parkingSpotService;
	
	@Mock
	private ParkingSpotRepository parkingSpotRepositoryMock;
	
	private ParkingSpotModel parkingSotToSave;
	private PageImpl<ParkingSpotModel> listParkingSpot;
	
	@BeforeEach
	void setUp() throws Exception {
		parkingSotToSave = ParkingSpotCreator.mockParkingSpot();
		listParkingSpot = new PageImpl<>(List.of(parkingSotToSave));
		
		// save
		BDDMockito.when(parkingSpotRepositoryMock.save(any(ParkingSpotModel.class)))
				.thenReturn(parkingSotToSave);
		
		// existsByLicensePlateCar
		BDDMockito.when(parkingSpotRepositoryMock.existsByLicensePlateCar(anyString())).thenReturn(false);
		
		// existsByParkingSpotNumber
		BDDMockito.when(parkingSpotRepositoryMock.existsByParkingSpotNumber(anyString())).thenReturn(false);
		
		// existsByApartmentAndBlock
		BDDMockito.when(parkingSpotRepositoryMock.existsByApartmentAndBlock(anyString(), anyString()))
				.thenReturn(false);
		
		// existsByApartment
		BDDMockito.when(parkingSpotRepositoryMock.existsByApartment(anyString())).thenReturn(false);
		
		// findAll
		BDDMockito.when(parkingSpotRepositoryMock.findAll(any(PageRequest.class))).thenReturn(listParkingSpot);
		
		// findById
		BDDMockito.when(parkingSpotRepositoryMock.findById(any(UUID.class)))
				.thenReturn(Optional.of(parkingSotToSave));
		
		// delete
		BDDMockito.doNothing().when(parkingSpotRepositoryMock).delete(any(ParkingSpotModel.class));
	}

	@Test
	@DisplayName("save insert parkingSpot when successful")
	public void save_InsertParkingSpot_WhenSuccessful() {
		ParkingSpotPostDto parkingSpotDto = ParkingSpotCreator.mockParkingSpotPostDto();
		ParkingSpotModel parkingSpot = parkingSpotService.save(parkingSpotDto);
		
		if (parkingSpot != null) {
			parkingSpot.setIdParking(parkingSotToSave.getIdParking());
		}
		
		assertAll(
				() -> assertNotNull(parkingSpot),
				() -> assertNotNull(parkingSpot.getIdParking()),
				() -> assertEquals(parkingSotToSave, parkingSpot)
		);
	}
	
	@Test
	@DisplayName("save throws ConstraintViolationException when parkingSpotDto have invalid fields")
	public void save_ThrowConstraintViolationException_WhenParkingSpotDtoHaveInvalidFields() {
		BDDMockito.when(parkingSpotRepositoryMock.save(any(ParkingSpotModel.class)))
				.thenThrow(ConstraintViolationException.class);
		
		ParkingSpotPostDto parkingSpotDto = ParkingSpotCreator.mockInvalidParkingSpotPostDto();
		
		assertThrows(ConstraintViolationException.class, () -> parkingSpotService.save(parkingSpotDto));
	}
	
	@Test
	@DisplayName("existsByLicensePlateCar return False when licensePlateCar not is in use")
	public void existsByLicensePlateCar_ReturnFalse_WhenLicensePlateCarNotIsInUse() {
		boolean existsByLicensePlateCar = parkingSpotService.existsByLicensePlateCar("123");
		
		assertFalse(existsByLicensePlateCar);
	}
	
	@Test
	@DisplayName("existsByLicensePlateCar return True when licensePlateCar is already in use")
	public void existsByLicensePlateCar_ReturnTrue_WhenLicensePlateCarIsAlreadyInUse() {
		BDDMockito.when(parkingSpotRepositoryMock.existsByLicensePlateCar(anyString())).thenReturn(true);
		boolean existsByLicensePlateCar = parkingSpotService.existsByLicensePlateCar("123");
		
		assertTrue(existsByLicensePlateCar);
	}
	
	@Test
	@DisplayName("existsByParkingSpotNumber return False when ParkingSpotNumber not is in use")
	public void existsByParkingSpotNumber_ReturnFalse_WhenParkingSpotNumberNotIsInUse() {
		boolean existsByParkingSpotNumber = parkingSpotService.existsByParkingSpotNumber("123");
		
		assertFalse(existsByParkingSpotNumber);
	}
	
	@Test
	@DisplayName("existsByParkingSpotNumber return True when ParkingSpotNumber is already in use")
	public void existsByParkingSpotNumber_ReturnTrue_WhenParkingSpotNumberIsAlreadyInUse() {
		BDDMockito.when(parkingSpotRepositoryMock.existsByParkingSpotNumber(anyString())).thenReturn(true);
		boolean existsByParkingSpotNumber = parkingSpotService.existsByParkingSpotNumber("123");
		
		assertTrue(existsByParkingSpotNumber);
	}
	
	@Test
	@DisplayName("existsByApartmentAndBlock return False when ApartmentAndBlock not is in use")
	public void existsByApartmentAndBlock_ReturnFalse_WhenApartmentAndBlockNotIsInUse() {
		boolean existsByApartmentAndBlock = parkingSpotService.existsByApartmentAndBlock("123", "456");
		
		assertFalse(existsByApartmentAndBlock);
	}
	
	@Test
	@DisplayName("existsByApartmentAndBlock return True when ApartmentAndBlock is already in use")
	public void existsByApartmentAndBlock_ReturnTrue_WhenApartmentAndBlockIsAlreadyInUse() {
		BDDMockito.when(parkingSpotRepositoryMock.existsByApartmentAndBlock(anyString(), anyString()))
				.thenReturn(true);
		boolean existsByApartmentAndBlock = parkingSpotService.existsByApartmentAndBlock("123", "456");
		
		assertTrue(existsByApartmentAndBlock);
	}
	
	@Test
	@DisplayName("existsByApartment return False when apartament not is in use")
	public void existsByApartment_ReturnFalse_WhenApartamentNotIsInUse() {
		boolean existsByApartment = parkingSpotService.existsApartament("123");
		
		assertFalse(existsByApartment);
	}
	
	@Test
	@DisplayName("existsByApartment return True when apartament is already in use")
	public void existsByApartment_ReturnTrue_WhenApartamentIsAlreadyInUse() {
		BDDMockito.when(parkingSpotRepositoryMock.existsByApartment(anyString())).thenReturn(true);
		boolean existsByApartment = parkingSpotService.existsApartament("123");
		
		assertTrue(existsByApartment);
	}

	@Test
	@DisplayName("findAll return list of parkingSpot inside page object when successful")
	public void findAll_ReturnListParkingSpotInsidePageObject_WhenSuccessful() {
		Page<ParkingSpotModel> findAll = parkingSpotService.findAll(PageRequest.of(1, 1));
		
		assertAll(
				() -> assertNotNull(findAll),
				() -> assertTrue(findAll.getContent().contains(parkingSotToSave))
		);
	}
	
	@Test
	@DisplayName("findAll return Empty list of parkingSpot inside page object when not found any parkingSpot")
	public void findAll_ReturnEmptyListParkingSpotInsidePageObject_WhenNotFoundAnyParkingSpot() {
		BDDMockito.when(parkingSpotRepositoryMock.findAll(any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of()));
		
		Page<ParkingSpotModel> findAll = parkingSpotService.findAll(PageRequest.of(1, 1));
		
		assertAll(
				() -> assertNotNull(findAll),
				() -> assertTrue(findAll.getContent().isEmpty())
		);
	}
	
	@Test
	@DisplayName("findByIdOrThrowsParkingSpotNotFoundException return parkingSpot when successful")
	public void findByIdOrThrowsParkingSpotNotFoundException_ReturnParkingSpot_WhenSuccessful() {
		ParkingSpotModel parkingSpotFound = parkingSpotService.
				findByIdOrThrowsParkingSpotNotFoundException(UUID.randomUUID());
		
		assertAll(
				() -> assertNotNull(parkingSpotFound),
				() -> assertEquals(parkingSotToSave, parkingSpotFound)
		);
	}
	
	@Test
	@DisplayName("findByIdOrThrowsParkingSpotNotFoundException Throw ParkingSpotNotFoundException when parkingSpot not found")
	public void findByIdOrThrowsParkingSpotNotFoundException_ThrowParkingSpotNotFoundException_WhenParkingSpotNotFound() {
		BDDMockito.when(parkingSpotRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(ParkingSpotNotFoundException.class, () -> parkingSpotService
				.findByIdOrThrowsParkingSpotNotFoundException(UUID.randomUUID()));
	}
	
	@Test
	@DisplayName("delete remove parkingSpot when successful")
	public void delete_RemoveParkingSpot_WhenSuccessful() {
		assertDoesNotThrow(() -> parkingSpotService.delete(UUID.randomUUID()));
	}
	
	@Test
	@DisplayName("delete Throws parkingSpotNotFoundException when parking spot not found by id")
	public void delete_ThrowsParkingSpotNotFoundException_WhenParkingSpotNotFoundById() {
		BDDMockito.when(parkingSpotRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		assertThrows(ParkingSpotNotFoundException.class, () -> parkingSpotService.delete(UUID.randomUUID()));
	}
	
	@Test
	@DisplayName("updated Replace parkingSpot when successful")
	public void updated_ReplaceParkingSpot_WhenSuccessful() {
		ParkingSpotPutDto parkingSpotPutDto = ParkingSpotCreator.mockParkingSpotPutDto();
		
		assertDoesNotThrow(() -> parkingSpotService.updated(UUID.randomUUID(), parkingSpotPutDto));
	}
	
	@Test
	@DisplayName("updated Throws parkingSpotNotFoundException when parking spot not found by id")
	public void updated_ThrowsParkingSpotNotFoundException_WhenParkingSpotNotFoundById() {
		BDDMockito.when(parkingSpotRepositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());
		
		ParkingSpotPutDto parkingSpotPutDto = ParkingSpotCreator.mockParkingSpotPutDto();
		
		assertThrows(ParkingSpotNotFoundException.class, () -> parkingSpotService.updated(UUID.randomUUID(),
				parkingSpotPutDto));
	}
	
	@Test
	@DisplayName("updated throws ConstraintViolationException when parkingSpotDto have invalid fields")
	public void updated_ThrowConstraintViolationException_WhenParkingSpotDtoHaveInvalidFields() {
		BDDMockito.when(parkingSpotRepositoryMock.save(any(ParkingSpotModel.class)))
				.thenThrow(ConstraintViolationException.class);
		
		ParkingSpotPutDto parkingSpotPutDto = ParkingSpotCreator.mockInvalidParkingSpotPutDto();
		
		assertThrows(ConstraintViolationException.class, () -> parkingSpotService.updated(UUID.randomUUID(),
				parkingSpotPutDto));
	}
	
	
}
