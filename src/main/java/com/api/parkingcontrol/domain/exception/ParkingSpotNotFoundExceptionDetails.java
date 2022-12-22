package com.api.parkingcontrol.domain.exception;

import com.api.parkingcontrol.api.handler.ExceptionDetails;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ParkingSpotNotFoundExceptionDetails extends ExceptionDetails {
}
