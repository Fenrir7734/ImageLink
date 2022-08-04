package com.fenrir.imagelink.controller.advice;

import com.fenrir.imagelink.exception.CodeGenerationException;
import com.fenrir.imagelink.exception.message.ConstraintViolationErrorMessage;
import com.fenrir.imagelink.exception.message.ConstraintViolationInfo;
import com.fenrir.imagelink.exception.message.ErrorMessage;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<ConstraintViolationInfo> constraintViolations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolationInfo::from)
                .toList();

        ErrorMessage message = new ConstraintViolationErrorMessage(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                "Constraint Violation",
                request.getDescription(false),
                constraintViolations
        );
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONTINUE.value(),
                LocalDateTime.now(),
                ex.getMostSpecificCause().getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ CodeGenerationException.class })
    public ResponseEntity<ErrorMessage> handleCodeGenerationException(CodeGenerationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorMessage> handleUnknownException(Exception ex, WebRequest request) {
        ex.printStackTrace();

        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                "Internal server error",
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
