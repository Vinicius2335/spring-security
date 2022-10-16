package com.api.parkingcontrol.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.parkingcontrol.dtos.ParkingSpotPostDto;
import com.api.parkingcontrol.dtos.ParkingSpotPutDto;
import com.api.parkingcontrol.exception.ParkingSpotNotFoundException;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotService;
import com.api.parkingcontrol.util.ParkingSpotCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for parking spot controller")
class ParkingSpotControllerTest {
	@InjectMocks
	private ParkingSpotController parkingSpotController;
	
	@Mock
	private ParkingSpotService parkingSpotServiceMock;
	
	private ParkingSpotModel parkingSpotToSave;

	@BeforeEach
	void setUp() throws Exception {
		parkingSpotToSave = ParkingSpotCreator.mockParkingSpot();
		
		// save
		BDDMockito.when(parkingSpotServiceMock.save(any(ParkingSpotPostDto.class)))
				.thenReturn(parkingSpotToSave);
		
		// findAll
		BDDMockito.when(parkingSpotServiceMock.findAll(any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of(parkingSpotToSave)));
		
		// findByIdOrThrowsParkingSpotNotFoundException
		BDDMockito.when(parkingSpotServiceMock
				.findByIdOrThrowsParkingSpotNotFoundException(any(UUID.class)))
				.thenReturn(parkingSpotToSave);
		
		// delete
		BDDMockito.doNothing().when(parkingSpotServiceMock).delete(any(UUID.class));
		
		// updated
		BDDMockito.doNothing().when(parkingSpotServiceMock).updated(any(UUID.class), 
				any(ParkingSpotPutDto.class));
		
	}

	@Test
	@DisplayName("save insert parkingSpot when successful")
	public void save_InsertParkingSpot_WhenSuccessful() {
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotCreator.mockParkingSpotPostDto();
		ResponseEntity<ParkingSpotModel> responseEntity = parkingSpotController
				.saveParkingSpot(parkingSpotPostDto);
		
		assertAll(
				() -> assertNotNull(responseEntity.getBody()),
				() -> assertEquals(CREATED, responseEntity.getStatusCode()),
				() -> assertEquals(parkingSpotToSave, responseEntity.getBody())
		);
	}
	
	@Test
	@DisplayName("save Throw ConstraintViolationException when parkingSpotDto have invalid fields")
	public void save_ThrowConstraintViolationException_WhenParkingSpotDtoHaveInvalidFields() {
		ParkingSpotPostDto invalidParkingSpotPostDto = ParkingSpotCreator.mockInvalidParkingSpotPostDto();
		BDDMockito.when(parkingSpotServiceMock.save(any(ParkingSpotPostDto.class)))
				.thenThrow(ConstraintViolationException.class);
		
		assertThrows(ConstraintViolationException.class, () -> parkingSpotController
				.saveParkingSpot(invalidParkingSpotPostDto));
	}
	
	@Test
	@DisplayName("getAllParkingSpot return list of parkingSpot inside page object when successful")
	public void getAllParkingSpot_ReturnListParkingSpotInsidePageObject_WhenSuccessful() {
		ResponseEntity<Page<ParkingSpotModel>> responseEntity = parkingSpotController
				.getAllParkingSpot(PageRequest.of(1, 1));
		
		assertAll(
				() -> assertNotNull(responseEntity.getBody()),
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertTrue(responseEntity.getBody().getContent().contains(parkingSpotToSave))
		);
	}
	
	@Test
	@DisplayName("getAllParkingSpot return Empt list of parkingSpot inside page object when no parkingSpot was found")
	public void getAllParkingSpot_ReturnEmptyListParkingSpotInsidePageObject_WhenNoParkingSpotFound() {
		BDDMockito.when(parkingSpotServiceMock.findAll(any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of()));
		
		ResponseEntity<Page<ParkingSpotModel>> responseEntity = parkingSpotController
				.getAllParkingSpot(PageRequest.of(1, 1));
		
		assertAll(
				() -> assertNotNull(responseEntity.getBody()),
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertTrue(responseEntity.getBody().getContent().isEmpty())
		);
	}
	
	@Test
	@DisplayName("getOneParkingSpot return a parkingSpot when successful")
	public void getOneParkingSpot_ReturnParkingSpot_WhenSuccessful() {
		ResponseEntity<ParkingSpotModel> responseEntity = parkingSpotController
				.getOneParkingSpot(UUID.randomUUID());
		
		assertAll(
				() -> assertNotNull(responseEntity.getBody()),
				() -> assertEquals(OK, responseEntity.getStatusCode()),
				() -> assertEquals(parkingSpotToSave, responseEntity.getBody())
		);
	}
	
	@Test
	@DisplayName("getOneParkingSpot Throws ParkingSpotNotFoundException when parkingSpot not found  by Id")
	public void getOneParkingSpot_ThrowParkingSpotNotFoundException_WhenParkingNotFoundById() {
		BDDMockito.when(parkingSpotServiceMock.findByIdOrThrowsParkingSpotNotFoundException(any(UUID.class)))
				.thenThrow(ParkingSpotNotFoundException.class);
		
		assertThrows(ParkingSpotNotFoundException.class, () -> parkingSpotController
				.getOneParkingSpot(UUID.randomUUID()));
	}

	@Test
	@DisplayName("deleteParkingSpot remove parkingControl when successful")
	public void deleteParkingSpot_RemoveParkingControl_WhenSuccessfu() {
		ResponseEntity<Object> deleteParkingSpot = parkingSpotController.deleteParkingSpot(UUID.randomUUID());
		
		assertAll(
				() -> assertNotNull(deleteParkingSpot.getBody()),
				() -> assertEquals(NO_CONTENT, deleteParkingSpot.getStatusCode()),
				() -> assertEquals("Parking Spot deleted successfully", deleteParkingSpot.getBody())
		);
	}
	
	@Test
	@DisplayName("deleteParkingSpot Throws ParkingSpotNotFoundException when parkingSpot not found by Id")
	public void deleteParkingSpot_ThrowParkingSpotNotFoundException_WhenParkingNotFoundById() {
		BDDMockito.doThrow(ParkingSpotNotFoundException.class).when(parkingSpotServiceMock)
				.delete(any(UUID.class));
		
		assertThrows(ParkingSpotNotFoundException.class, () -> parkingSpotController
				.deleteParkingSpot(UUID.randomUUID()));
	}
	
	@Test
	@DisplayName("updateParkingSpot replace parkingSpot when successful")
	public void updateParkingSpot_ReplaceParkingSpot_WhenSuccessful() {
		ParkingSpotPutDto parkingSpotPutDto = ParkingSpotCreator.mockParkingSpotPutDto();
		ResponseEntity<Object> updateParkingSpot = parkingSpotController.updateParkingSpot(UUID.randomUUID(),
				parkingSpotPutDto);
		
		assertAll(
				() -> assertNotNull(updateParkingSpot.getBody()),
				() -> assertEquals(NO_CONTENT, updateParkingSpot.getStatusCode()),
				() -> assertEquals("Parking Spot updated successfully", updateParkingSpot.getBody())
		);
	}
	
	@Test
	@DisplayName("updateParkingSpot Throws ParkingSpotNotFoundException when parkingSpot not found by Id")
	public void updateParkingSpot_ThrowParkingSpotNotFoundException_WhenParkingNotFoundById() {
		BDDMockito.doThrow(ParkingSpotNotFoundException.class).when(parkingSpotServiceMock)
				.updated(any(UUID.class), any(ParkingSpotPutDto.class));
		
		ParkingSpotPutDto invalidParkingSpotPutDto = ParkingSpotCreator.mockInvalidParkingSpotPutDto();
		
		assertThrows(ParkingSpotNotFoundException.class, () -> parkingSpotController
				.updateParkingSpot(UUID.randomUUID(), invalidParkingSpotPutDto));
	}
	
	@Test
	@DisplayName("updateParkingSpot Throw ConstraintViolationException when parkingSpotDto have invalid fields")
	public void updateParkingSpot_ThrowConstraintViolationException_WhenParkingSpotDtoHaveInvalidFields() {
		ParkingSpotPutDto invalidParkingSpotDto = ParkingSpotCreator.mockInvalidParkingSpotPutDto();
		BDDMockito.doThrow(ConstraintViolationException.class).when(parkingSpotServiceMock)
				.updated(any(UUID.class), any(ParkingSpotPutDto.class));
		
		assertThrows(ConstraintViolationException.class, () -> parkingSpotController
				.updateParkingSpot(UUID.randomUUID(), invalidParkingSpotDto));
	}
	
	
}
