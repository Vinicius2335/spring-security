package com.api.parkingcontrol.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpotModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Type(type="uuid-char")
	@Column(columnDefinition = "VARCHAR(36)")
	// @Column(name = "id") -> para nao dar erro quando o nome do campo for id
	private UUID idParking;
	
	@Column(nullable = false, unique = true, length = 10)
	private String parkingSpotNumber;
	
	@Column(nullable = false, unique = true, length = 7)
	private String licensePlateCar;
	
	@Column(nullable = false, length = 70)
	private String brandCar;
	
	@Column(nullable = false, length = 70)
	private String modelCar;
	
	@Column(nullable = false, length = 70)
	private String colorCar;
	
	@Column(nullable = false)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD 'T'HH:mm:ss[.SS]'Z'")
	private LocalDateTime registrationDate;
	
	@Column(nullable = false, length = 130)
	private String responsibleName;
	
	@Column(nullable = false, length = 30)
	private String apartment;
	
	@Column(nullable = false, length = 30)
	private String block;
	
}
