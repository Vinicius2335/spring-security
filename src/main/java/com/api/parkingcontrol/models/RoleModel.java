package com.api.parkingcontrol.models;

import com.api.parkingcontrol.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_ROLE")
public class RoleModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type="uuid-char")
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID idRole;

    //@Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private String roleName;

}
