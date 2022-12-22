package com.api.parkingcontrol.api.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.parkingcontrol.domain.exception.InvalidCredentialException;
import com.api.parkingcontrol.domain.exception.InvalidCredentialExceptionDetails;
import com.api.parkingcontrol.domain.exception.ParkingSpotNotFoundException;
import com.api.parkingcontrol.domain.exception.ParkingSpotNotFoundExceptionDetails;
import com.api.parkingcontrol.domain.exception.ValidationExceptionDetails;

@ControllerAdvice
public class RestExcepionHandler {

    @ExceptionHandler(ParkingSpotNotFoundException.class)
    public ResponseEntity<ParkingSpotNotFoundExceptionDetails> handlerParkingSpotNotFoundException(ParkingSpotNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ParkingSpotNotFoundExceptionDetails.builder()
                        .title("Parking Spot Not Found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .details(exception.getMessage())
                        .develloperMessage(exception.getClass().getName())
                        .timestamp(LocalDateTime.now()).build());
    }
    
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<InvalidCredentialExceptionDetails> handlerInvalidCredentialException(InvalidCredentialException exception) {
    	return ResponseEntity
    			.status(HttpStatus.UNAUTHORIZED)
    			.body(InvalidCredentialExceptionDetails.builder()
    					.title("Invalid Login")
    					.status(HttpStatus.UNAUTHORIZED.value())
    					.details(exception.getMessage())
    					.develloperMessage(exception.getClass().getName())
    					.timestamp(LocalDateTime.now()).build());
    	
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));

        String fieldsMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ValidationExceptionDetails.builder()
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .title("Bad Request Exception, Invalid Fields")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details("Checked Fields")
                        .develloperMessage(exception.getClass().getName())
                        .timestamp(LocalDateTime.now()).build());
    }
}
