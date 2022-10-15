package com.api.parkingcontrol.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.api.parkingcontrol.enums.RoleName;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repository.RoleRepositoryTest;
import com.api.parkingcontrol.repository.UserRepository;
import com.api.parkingcontrol.service.ParkingSpotService;
import com.api.parkingcontrol.util.ParkingSpotCreator;
import com.api.parkingcontrol.util.UserCreator;

/* Devido ao problema encontrado com o banco h2 na relaçao manyToMany que nao deixava 
 * realizar o insert into tb_users_roles (user_id, role_id) values (?, ?)
 * vai ter que ser com o banco de dados de produção
 * 
 * 2022-10-15 14:28:42.484  WARN 2372 --- [main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 23506,
 * SQLState: 23506
 * 
 * 2022-10-15 14:28:42.485 ERROR 2372 --- [main] o.h.engine.jdbc.spi.SqlExceptionHelper   : Referential
 * integrity constraint violation: "FK6P4O2KXBQ23RTHM174K19XO2H: PUBLIC.TB_USERS_ROLES FOREIGN KEY(ROLE_ID)
 * REFERENCES PUBLIC.TB_ROLE(ID_ROLE) (U&'e&\\fffd\\fffd\\fffd3H\\fffd\\fffd^<\\fffdO//\\fffd')"; 
 * SQL statement:
 * insert into tb_users_roles (user_id, role_id) values (?, ?) [23506-214]
 */

//@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration test for parking spot controller")
class ParkingSpotControllerIT {
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@MockBean
	private ParkingSpotService parkingSpotService;
	
//	@Autowired
//	private RoleRepositoryTest roleRepositoryTest;
//	
//	@Autowired
//	private UserRepository userRepository;
	
	private String userToken;
	private String adminToken;
	private static final String ENDPOINT = "/parking-spot";
	private ParkingSpotModel parkingSpotToSave;
//	private UserModel user;
//	private UserModel admin;
//	private UserModel user2;
	private HttpEntity<Void> userValidAuthentication;
	
	@BeforeEach
	void setUp() throws Exception {
		parkingSpotToSave = ParkingSpotCreator.mockParkingSpot();
		
//		RoleModel adminRole = roleRepositoryTest.save(RoleModel.builder()
//				.roleName(RoleName.ROLE_ADMIN).build());
//		RoleModel userRole = roleRepositoryTest.save(RoleModel.builder()
//				.roleName(RoleName.ROLE_USER).build());
//		
//		user = userRepository.save(UserCreator.mockUser());
//		user.setRoles(List.of(userRole));
//		
//		admin = userRepository.save( UserCreator.mockUserAdmin());
//		admin.setRoles(List.of(adminRole));
//		
//		user2 = userRepository.save(UserCreator.mockUser2Role());
//		user2.setRoles(List.of(userRole, adminRole));
//		
//		System.out.println(roleRepositoryTest.findAllUserRoles());
//		System.out.println(user.getIdUser());
//		System.out.println(userRole.getIdRole());
//		
//		roleRepositoryTest.saveUserToRole(user.getIdUser(), userRole.getIdRole());
//		roleRepositoryTest.saveUserToRole(admin.getIdUser(), adminRole.getIdRole());
//		
//		roleRepositoryTest.saveUserToRole(user2.getIdUser(), userRole.getIdRole());
//		roleRepositoryTest.saveUserToRole(user2.getIdUser(), adminRole.getIdRole());
		
//		userValidAuthentication = getUserValidAuthentication(getToken(user2.getUsername(), "onepiece"));
		
		
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
	
	public HttpEntity<Void> getUserValidAuthentication(String token){
		userToken = "Bearer " + token;
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, userToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Void>(headers);
	}
	
//	public HttpEntity<Void> getAdminValidAuthentication(){
//		adminToken = "Bearer " + getToken(admin.getUsername(), admin.getPassword());
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(HttpHeaders.AUTHORIZATION, this.adminToken);
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		return new HttpEntity<Void>(headers);
//	}
	
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
		
		HttpEntity<ParkingSpotModel> httpEntity = new HttpEntity<>(parkingSpotToSave,
				userValidAuthentication.getHeaders());
		
		ResponseEntity<ParkingSpotModel> exchange = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST,
				httpEntity, ParkingSpotModel.class);
		
		if (exchange.getBody().getIdParking() != null) {
			parkingSpotToSave.setIdParking(exchange.getBody().getIdParking());
		}
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
				() -> assertEquals(parkingSpotToSave, exchange.getBody())
		);
		
	}

}
