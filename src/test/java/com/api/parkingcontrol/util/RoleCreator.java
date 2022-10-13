package com.api.parkingcontrol.util;

import java.util.UUID;

import com.api.parkingcontrol.enums.RoleName;
import com.api.parkingcontrol.models.RoleModel;

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
