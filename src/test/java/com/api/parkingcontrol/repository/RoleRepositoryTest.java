package com.api.parkingcontrol.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.api.parkingcontrol.models.RoleModel;

public interface RoleRepositoryTest extends JpaRepository<RoleModel, UUID>{
	@Transactional
	@Modifying
	@Query(value = "insert into tb_users_roles (user_id, role_id) values (?1, ?2)", nativeQuery = true)
	void saveUserToRole(UUID idUser, UUID idRole);
	
//	@Transactional
//	@Modifying
//	@Query(value = "insert into tb_users_roles (user_id, role_id) values (:idUser, :idRole)", nativeQuery = true)
//	void saveUserToRole(UUID idUser, UUID idRole);
	
	@Query(value = "select * from tb_users_roles", nativeQuery = true)
	Collection findAllUserRoles();
	
//	@Transactional
//	@Modifying
//	@Query(value = "insert into tb_users_roles (user_id, role_id) values (:user_id, :role_id)",
//	nativeQuery = true)
//	void saveUserToRole(@Param("user_id")UUID idUser, @Param("role_id") UUID idRole);
}
