package com.api.parkingcontrol.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.api.parkingcontrol.enums.RoleName;
import com.api.parkingcontrol.models.RoleModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repository.RoleRepositoryTest;
import com.api.parkingcontrol.repository.UserRepository;
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
	
	private String userToken;
	private String adminToken;
	private static final String ENDPOINT = "/parking-spot";
	
	@BeforeEach
	void setUp() throws Exception {
		RoleModel adminRole = roleRepositoryTest.save(RoleModel.builder()
				.roleName(RoleName.ROLE_USER).build());
		RoleModel userRole = roleRepositoryTest.save(RoleModel.builder()
				.roleName(RoleName.ROLE_ADMIN).build());
		
		UserModel user = UserCreator.mockUser();
		user.setRoles(List.of(userRole));
		userRepository.save(user);
		
		UserModel admin = UserCreator.mockUserAdmin();
		admin.setRoles(List.of(adminRole));
		userRepository.save(admin);
		
		userToken = "Bearer " + getToken(user.getUsername(), user.getPassword());
		adminToken = "Bearer " + getToken(admin.getUsername(), admin.getPassword());
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
	
	public HttpEntity<Void> getUserValidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, this.userToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Void>(headers);
	}
	
	public HttpEntity<Void> getAdminValidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, this.adminToken);
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
	@DisplayName("login return status code 400 when insert a invalid login")
	public void login_Return400_WhenInsertInvalidLogin() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "ajsjdja");
		headers.add("password", "jasdjnusn");
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(headers);
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/login",
				HttpMethod.POST, httpEntity, Void.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
	}

}
