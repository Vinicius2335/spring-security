package com.api.parkingcontrol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;

@Mapper
public abstract class ParkingSpotMapper {
	public final static ParkingSpotMapper INSTANCE = Mappers.getMapper(ParkingSpotMapper.class);
	
	public abstract ParkingSpotModel toParkingSpotModel(ParkingSpotDto parkingSpotDto);
}
