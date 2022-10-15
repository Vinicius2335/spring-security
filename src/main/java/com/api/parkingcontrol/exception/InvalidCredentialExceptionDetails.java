package com.api.parkingcontrol.exception;

import com.api.parkingcontrol.handler.ExceptionDetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InvalidCredentialExceptionDetails extends ExceptionDetails{
}
