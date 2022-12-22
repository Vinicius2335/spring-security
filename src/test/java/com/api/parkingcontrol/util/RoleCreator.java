package com.api.parkingcontrol.util;

import java.util.UUID;

import com.api.parkingcontrol.domain.enums.RoleName;
import com.api.parkingcontrol.domain.models.RoleModel;

public class RoleCreator {
	public static RoleModel mockRoleUser() {
		return RoleModel.builder()
				.idRole(UUID.randomUUID())
				.roleName(RoleName.ROLE_USER).build();
	}
	
	public static RoleModel mockRoleAdmin() {
		return RoleModel.builder()
				.idRole(UUID.randomUUID())
				.roleName(RoleName.ROLE_ADMIN).build();
	}
}
