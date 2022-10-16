package com.api.parkingcontrol.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.parkingcontrol.models.RoleModel;

public interface RoleRepositoryTest extends JpaRepository<RoleModel, UUID>{
	
}
