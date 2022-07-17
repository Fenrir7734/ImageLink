package com.fenrir.imagelink.exception.message;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ConstraintViolationErrorMessage extends ErrorMessage {
    private final List<ConstraintViolationInfo> constraintViolations;

    public ConstraintViolationErrorMessage(
            int statusCode,
            LocalDateTime timestamp,
            String message,
            String description,
            List<ConstraintViolationInfo> constraintViolations) {

        super(statusCode, timestamp, message, description);
        this.constraintViolations = constraintViolations;
    }
}
