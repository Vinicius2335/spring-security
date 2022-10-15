package com.api.parkingcontrol.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.api.parkingcontrol.controller.ParkingSpotController;
import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.enums.RoleName;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repository.RoleRepositoryTest;
import com.api.parkingcontrol.repository.UserRepository;
import com.api.parkingcontrol.service.ParkingSpotService;
import com.api.parkingcontrol.util.ParkingSpotCreator;
import com.api.parkingcontrol.util.UserCreator;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration test for parking spot controller")
class ParkingSpotControllerITv2 {
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@MockBean
	private ParkingSpotService parkingSpotService;
	
	private static final String ENDPOINT = "/parking-spot";
	private ParkingSpotModel parkingSpotToSave;
	private HttpEntity<Void> userValidAuthentication;
	private HttpEntity<Void> adminValidAuthentication;
	
	@BeforeEach
	void setUp() throws Exception {
		parkingSpotToSave = ParkingSpotCreator.mockParkingSpot();
		
		adminValidAuthentication = getValidAuthentication(getToken("vinicius", "devdojo"));
		userValidAuthentication = getValidAuthentication(getToken("goku", "vinicius"));
		
		BDDMockito.when(parkingSpotService.save(ArgumentMatchers.any(ParkingSpotDto.class)))
				.thenReturn(parkingSpotToSave);
	}
	
	public String getToken(String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", username);
		headers.add("password", password);
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(headers);
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/login",
				HttpMethod.POST, httpEntity, Void.class);
		
		return exchange.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
	}
	
	public HttpEntity<Void> getValidAuthentication(String token){
		String userToken = "Bearer " + token;
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, userToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Void>(headers);
	}
	
	public HttpEntity<Void> getInvalidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "11111");
		return new HttpEntity<Void>(headers);
	}

	@Test
	@DisplayName("login return status code 200 when login successfully")
	public void login_Return200_WhenLoginSuccessfully() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "vinicius");
		headers.add("password", "devdojo");
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(headers);
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/login",
				HttpMethod.POST, httpEntity, Void.class);
		
		assertEquals(HttpStatus.OK, exchange.getStatusCode());
	}
	
	// BUG: SAINDO STATUS CODE 500
	@Test
	@DisplayName("login return status code 401 when insert a invalid login")
	public void login_Return401_WhenInsertInvalidLogin() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "ajsjdja");
		headers.add("password", "jasdjnusn");
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(headers);
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/login",
				HttpMethod.POST, httpEntity, Void.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}
	
	@Test
	@DisplayName("saveParkingSpot saveParkingSpot when successful")
	public void saveParkingSpot_InsertParkingControl_WhenSuccessful() {
		ParkingSpotDto parkingSpotDto = ParkingSpotCreator.mockParkingSpotDto();
		
		HttpEntity<ParkingSpotDto> httpEntity = new HttpEntity<>(parkingSpotDto,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<ParkingSpotModel> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, ParkingSpotModel.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
				() -> assertEquals(parkingSpotDto.getLicensePlateCar(), exchange.getBody()
						.getLicensePlateCar())
		);
		
	}

}
