package com.api.parkingcontrol.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.api.parkingcontrol.core.config.wrappers.RestResponsePage;
import com.api.parkingcontrol.api.representation.model.ParkingSpotPostDto;
import com.api.parkingcontrol.api.representation.model.ParkingSpotPutDto;
import com.api.parkingcontrol.domain.enums.RoleName;
import com.api.parkingcontrol.domain.models.ParkingSpotModel;
import com.api.parkingcontrol.domain.models.RoleModel;
import com.api.parkingcontrol.domain.models.UserModel;
import com.api.parkingcontrol.domain.repository.ParkingSpotRepository;
import com.api.parkingcontrol.repository.RoleRepositoryTest;
import com.api.parkingcontrol.domain.repository.UserRepository;
import com.api.parkingcontrol.util.ParkingSpotCreator;
import com.api.parkingcontrol.util.UserCreator;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration test for parking spot controller")
class ParkingSpotControllerIT {
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private RoleRepositoryTest roleRepositoryTest;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ParkingSpotRepository parkingSpotRepository;
	
	private static final String ENDPOINT = "/parking-spot";
	private ParkingSpotModel parkingSpotToSave;
	
	private UserModel user;
	private UserModel admin;
	
	private HttpEntity<Void> userValidAuthentication;
	private HttpEntity<Void> adminValidAuthentication;
	
	@BeforeEach
	void setUp() throws Exception {
		parkingSpotToSave = ParkingSpotCreator.mockParkingSpot();
		
		RoleModel adminRole = roleRepositoryTest.save(RoleModel.builder()
				.roleName(RoleName.ROLE_ADMIN).build());
		RoleModel userRole = roleRepositoryTest.save(RoleModel.builder()
				.roleName(RoleName.ROLE_USER).build());
		
		user = UserCreator.mockUser();
		user.setRoles(List.of(userRole));
		user = userRepository.save(user);
		
		admin = UserCreator.mockUserAdmin();
		admin.setRoles(List.of(adminRole));
		admin = userRepository.save(admin);
		
		userValidAuthentication = getValidAuthentication(getToken(user.getUsername(), "vinicius"));
		adminValidAuthentication = getValidAuthentication(getToken(admin.getUsername(), "devdojo"));
		
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
	@DisplayName("login return status code 200 when ADMIN login successfully")
	public void login_Return200_WhenAdminLoginSuccessfully() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "vinicius");
		headers.add("password", "devdojo");
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(headers);
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/login",
				HttpMethod.POST, httpEntity, Void.class);
		
		assertEquals(HttpStatus.OK, exchange.getStatusCode());
	}
	
	@Test
	@DisplayName("login return status code 200 when USER login successfully")
	public void login_Return200_WhenUserLoginSuccessfully() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "goku");
		headers.add("password", "vinicius");
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
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange("/login",
				HttpMethod.POST, httpEntity, Object.class);
		
		System.out.println(exchange.getBody());
		
		assertEquals(HttpStatus.UNAUTHORIZED, exchange.getStatusCode());
	}
	
	@Test
	@DisplayName("saveParkingSpot ADMIN insert parkingSpot when successful")
	public void saveParkingSpot_AdminInsertParkingSpot_WhenSuccessful() {
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotCreator.mockParkingSpotPostDto();
		
		HttpEntity<ParkingSpotPostDto> httpEntity = new HttpEntity<>(parkingSpotPostDto,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<ParkingSpotModel> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, ParkingSpotModel.class);
		
		if (exchange.getBody().getIdParking() != null) {
			parkingSpotToSave.setIdParking(exchange.getBody().getIdParking());
			parkingSpotToSave.setRegistrationDate(exchange.getBody().getRegistrationDate());
		}
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
				() -> assertEquals(parkingSpotToSave, exchange.getBody())
		);
		
	}
	
	@Test
	@DisplayName("saveParkingSpot return 400 when license plate car is already in use")
	public void saveParkingSpot_Return400_WhenLicensePlateCarIsAlreadyInUse() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotPostDto.builder().apartment("71").block("b").brandCar("111")
				.colorCar("verde").licensePlateCar(save.getLicensePlateCar()).modelCar("novo")
				.parkingSpotNumber("800").responsibleName("Zangetsu").build();
		
		HttpEntity<ParkingSpotPostDto> httpEntity = new HttpEntity<>(parkingSpotPostDto,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("saveParkingSpot return 400 when parking Spot Number is already in use")
	public void saveParkingSpot_Return400_WhenParkingSpotNumberIsAlreadyInUse() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotPostDto.builder().apartment("71").block("b").brandCar("111")
				.colorCar("verde").licensePlateCar("222").modelCar("novo").responsibleName("Zangetsu")
				.parkingSpotNumber(save.getParkingSpotNumber()).build();
		
		HttpEntity<ParkingSpotPostDto> httpEntity = new HttpEntity<>(parkingSpotPostDto,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("saveParkingSpot return 400 when is already in use")
	public void saveParkingSpot_Return400_WhenApartamentIsAlreadyInUse() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotPostDto.builder().apartment(save.getApartment()).block("b")
				.brandCar("111").colorCar("verde").licensePlateCar("222").modelCar("novo")
				.responsibleName("Zangetsu").build();
		
		HttpEntity<ParkingSpotPostDto> httpEntity = new HttpEntity<>(parkingSpotPostDto,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("saveParkingSpot return 400 when parkingSpotPutDto have invalid fields")
	public void saveParkingSpot_Return400_WhenparkingSpotPutDtoHaveInvalidFields() {
		parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotCreator.mockInvalidParkingSpotPostDto();
		
		HttpEntity<ParkingSpotPostDto> httpEntity = new HttpEntity<>(parkingSpotPostDto,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("saveParkingSpot return status code 403 when USER dont have permission")
	public void saveParkingSpot_Return403_WhenUserDontHavePermission() {
		ParkingSpotPostDto parkingSpotPostDto = ParkingSpotCreator.mockParkingSpotPostDto();
		
		HttpEntity<ParkingSpotPostDto> httpEntity = new HttpEntity<>(parkingSpotPostDto,
				userValidAuthentication.getHeaders());
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, Void.class);
		
		assertAll(
				() -> assertNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("getAllParkingSpot return a list parkingSpot inside page object when successful with ADMIN")
	public void getAllParkingSpot_ReturnListParkingSpotInsidePageObject_WhenSuccessfulWithAdmin() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
				ParameterizedTypeReference<RestResponsePage<ParkingSpotModel>> typeReference =
						new ParameterizedTypeReference<RestResponsePage<ParkingSpotModel>>() {
		};
		
		ResponseEntity<RestResponsePage<ParkingSpotModel>> exchange = testRestTemplate.exchange(ENDPOINT,
				HttpMethod.GET, adminValidAuthentication, typeReference);
		
		if(exchange.getBody() != null && exchange.getBody().getContent().size() == 1) {
			save.setRegistrationDate(exchange.getBody().getContent().get(0).getRegistrationDate());
		}
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(1, exchange.getBody().getContent().size()),
				() -> assertTrue(exchange.getBody().getContent().contains(save))
		);
		
	}
	
	@Test
	@DisplayName("getAllParkingSpot return a list parkingSpot inside page object when successful with USER")
	public void getAllParkingSpot_ReturnListParkingSpotInsidePageObject_WhenSuccessfulWithUser() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ParameterizedTypeReference<RestResponsePage<ParkingSpotModel>> typeReference =
				new ParameterizedTypeReference<RestResponsePage<ParkingSpotModel>>() {
		};
		
		ResponseEntity<RestResponsePage<ParkingSpotModel>> exchange = testRestTemplate.exchange(ENDPOINT,
				HttpMethod.GET, userValidAuthentication, typeReference);
		
		if(exchange.getBody() != null && exchange.getBody().getContent().size() == 1) {
			save.setRegistrationDate(exchange.getBody().getContent().get(0).getRegistrationDate());
		}
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(1, exchange.getBody().getContent().size()),
				() -> assertTrue(exchange.getBody().getContent().contains(save))
		);
	}
	
	@Test
	@DisplayName("getOneParkingSpot return a parkingSpot found by id when successful with ADMIN")
	public void getOneParkingSpot_ReturnParkingSpotFoundById_WhenSuccessfulWithAdmin() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ResponseEntity<ParkingSpotModel> exchange = testRestTemplate.exchange(ENDPOINT + "/" + save.getIdParking(),
				HttpMethod.GET, adminValidAuthentication, ParkingSpotModel.class);
		
		if (exchange.getBody() != null) {
			save.setRegistrationDate(exchange.getBody().getRegistrationDate());
		}
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(save, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("getOneParkingSpot return a parkingSpot found by id when successful with USER")
	public void getOneParkingSpot_ReturnParkingSpotFoundById_WhenSuccessfulWithUser() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ResponseEntity<ParkingSpotModel> exchange = testRestTemplate.exchange(ENDPOINT + "/" + save.getIdParking(),
				HttpMethod.GET, userValidAuthentication, ParkingSpotModel.class);
		
		if (exchange.getBody() != null) {
			save.setRegistrationDate(exchange.getBody().getRegistrationDate());
		}
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(save, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("getOneParkingSpot return 404 when parkingSpot not found")
	public void getOneParkingSpot_Return404_WhenParkingSpotNotFound() {
 ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT + "/" + UUID.randomUUID(),
				HttpMethod.GET, adminValidAuthentication, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("deleteParkingSpot remove a parkingSpot when successful with ADMIN")
	public void deleteParkingSpot_RemoveParkingSpot_WhenSuccessfulWithAdmin() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		
		ResponseEntity<ParkingSpotModel> exchange = testRestTemplate.exchange(ENDPOINT + "/" + save.getIdParking(),
				HttpMethod.DELETE, adminValidAuthentication, ParkingSpotModel.class);
		
		Optional<ParkingSpotModel> findById = parkingSpotRepository.findById(save.getIdParking());
		
		assertAll(
				() -> assertNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode()),
				() -> assertTrue(findById.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteParkingSpot return 404 when parkingSpot not found")
	public void deleteParkingSpot_Return404_WhenParkingSpotNotFound() {
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT + "/" + UUID.randomUUID(),
				HttpMethod.DELETE, adminValidAuthentication, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("deleteParkingSpot return 403 when user dont have permission")
	public void deleteParkingSpot_Return403_WhenUserDontHavePermission() {
		ResponseEntity<Void> exchange = testRestTemplate.exchange(ENDPOINT + "/" + UUID.randomUUID(),
				HttpMethod.DELETE, userValidAuthentication, Void.class);
		
		assertAll(
				() -> assertNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("updateParkingSpot update a parkingSpot when successful with ADMIN")
	public void updateParkingSpot_UpdateParkingSpot_WhenSuccessfulWithAdmin() {
		ParkingSpotModel save = parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		ParkingSpotPutDto parkingSpotPutDtoToUpdated = ParkingSpotCreator.mockParkingSpotDtoToUpdated();
		
		HttpEntity<ParkingSpotPutDto> httpEntity = new HttpEntity<>(parkingSpotPutDtoToUpdated,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT + "/" + save.getIdParking(),
				HttpMethod.PUT, httpEntity, Object.class);
		
		Optional<ParkingSpotModel> findById = parkingSpotRepository.findById(save.getIdParking());
		
		assertAll(
				() -> assertNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode()),
				() -> assertEquals(parkingSpotPutDtoToUpdated.getResponsibleName(), findById.get().getResponsibleName())
		);
	}
	
	@Test
	@DisplayName("updateParkingSpot return 404 when parkingSpot not found")
	public void updateParkingSpot_Return404_WhenParkingSpotNotFound() {
		parkingSpotRepository.save(ParkingSpotCreator.mockParkingSpot());
		ParkingSpotPutDto parkingSpotPutDtoToUpdated = ParkingSpotCreator.mockParkingSpotDtoToUpdated();
		
		HttpEntity<ParkingSpotPutDto> httpEntity = new HttpEntity<>(parkingSpotPutDtoToUpdated,
				adminValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT + "/" + UUID.randomUUID(),
				HttpMethod.PUT, httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("updateParkingSpot return 403 when user dont have permission")
	public void updateParkingSpot_Return403_WhenUserDontHavePermission() {
		ParkingSpotPutDto parkingSpotPutDtoToUpdated = ParkingSpotCreator.mockParkingSpotDtoToUpdated();
		
		HttpEntity<ParkingSpotPutDto> httpEntity = new HttpEntity<>(parkingSpotPutDtoToUpdated,
				userValidAuthentication.getHeaders());
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange(ENDPOINT + "/" + UUID.randomUUID(),
				HttpMethod.PUT, httpEntity, Void.class);
		
		assertAll(
				() -> assertNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("updateParkingSpot return 400 when parkingSpotPutDto have invalid fields")
	public void updateParkingSpot_Return400_WhenParkingSpotPutDtoHaveInvalidFields() {
		ParkingSpotPutDto parkingSpotPutDtoToUpdated = ParkingSpotCreator.mockInvalidParkingSpotPutDto();
		
		HttpEntity<ParkingSpotPutDto> httpEntity = new HttpEntity<>(parkingSpotPutDtoToUpdated,
				userValidAuthentication.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(ENDPOINT + "/" + UUID.randomUUID(),
				HttpMethod.PUT, httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
}
