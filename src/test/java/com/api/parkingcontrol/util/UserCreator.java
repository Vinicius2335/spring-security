package com.api.parkingcontrol.util;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api.parkingcontrol.models.UserModel;

public class UserCreator {
	public static UserModel mockUser() {
		return UserModel.builder()
				.idUser(UUID.randomUUID())
				.username("Ikki")
				.password(new BCryptPasswordEncoder().encode("devdojo")).build();
	}
	
	public static UserModel mockUserAdmin() {
		return UserModel.builder()
				.idUser(UUID.randomUUID())
				.username("Goku")
				.password(new BCryptPasswordEncoder().encode("vinicius")).build();
	}
}
