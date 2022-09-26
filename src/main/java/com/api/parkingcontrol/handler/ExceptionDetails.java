package com.api.parkingcontrol.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
public class ExceptionDetails {
    protected String title;
    protected int status;
    protected String details;
    protected String develloperMessage;
    protected LocalDateTime timestamp;
}
