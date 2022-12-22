package com.api.parkingcontrol.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.api.parkingcontrol.api.representation.model.ParkingSpotPostDto;
import com.api.parkingcontrol.api.representation.model.ParkingSpotPutDto;
import com.api.parkingcontrol.domain.models.ParkingSpotModel;

@Mapper
public abstract class ParkingSpotMapper {
	public final static ParkingSpotMapper INSTANCE = Mappers.getMapper(ParkingSpotMapper.class);
	
	public abstract ParkingSpotModel toParkingSpotModel(ParkingSpotPostDto parkingSpotPostDto);
	public abstract ParkingSpotModel toParkingSpotModel(ParkingSpotPutDto parkingSpotPutDto);
}
