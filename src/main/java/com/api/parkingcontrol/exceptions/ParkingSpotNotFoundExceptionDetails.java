package com.api.parkingcontrol.exceptions;

import com.api.parkingcontrol.handler.ExceptionDetails;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ParkingSpotNotFoundExceptionDetails extends ExceptionDetails {
}
