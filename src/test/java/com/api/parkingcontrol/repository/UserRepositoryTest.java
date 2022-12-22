package com.api.parkingcontrol.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import com.api.parkingcontrol.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.parkingcontrol.domain.models.RoleModel;
import com.api.parkingcontrol.domain.models.UserModel;
import com.api.parkingcontrol.util.RoleCreator;
import com.api.parkingcontrol.util.UserCreator;

@DataJpaTest
@DisplayName("Test for user repository")
class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepositoryTest roleRepository;
	
	private UserModel userToSave;
	private RoleModel userRole;
	private List<RoleModel> listRole;
	
	@BeforeEach
	void setUp() throws Exception {
		userRole = RoleCreator.mockRoleUser();
		
		RoleModel save = roleRepository.save(userRole);
		
		listRole = List.of(save);
		userToSave = UserCreator.mockUser();
		userToSave.setRoles(listRole);
	}

	@Test
	@DisplayName("save insert user when successful")
	public void save_InsertUser_WhenSuccessful() {
		UserModel user = userRepository.save(userToSave);
		
		if (user.getIdUser() != null) {
			userToSave.setIdUser(user.getIdUser());
		}
		
		assertAll(
				() -> assertNotNull(user),
				() -> assertEquals(userToSave, user)
		);
	}
	
	@Test
	@DisplayName("findByUsername return userModel when successful")
	public void findByUsername_ReturnUserModel_WhenSuccessful() {
		UserModel user = userRepository.save(userToSave);
		
		Optional<UserModel> userFound = userRepository.findByUsername(user.getUsername());
		
		assertAll(
				() -> assertFalse(userFound.isEmpty()),
				() -> assertEquals(user, userFound.get())
		);
	}
	
	@Test
	@DisplayName("findByUsername return empty when user not found by username")
	public void findByUsername_ReturnEmpty_WhenUserNotFoundByUsername() {
		Optional<UserModel> userFound = userRepository.findByUsername(userToSave.getUsername());
		
		assertTrue(userFound.isEmpty());
	}
}
