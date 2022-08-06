package com.fenrir.imagelink.controller.advice;

import com.fenrir.imagelink.exception.CodeGenerationException;
import com.fenrir.imagelink.exception.message.ConstraintViolationErrorMessage;
import com.fenrir.imagelink.exception.message.ConstraintViolationInfo;
import com.fenrir.imagelink.exception.message.ErrorMessage;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler({ ConstraintViolationException.class, MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(Exception ex, WebRequest request) {
        List<ConstraintViolationInfo> constraintViolations = null;

        if (ex instanceof ConstraintViolationException cve) {
            constraintViolations = cve.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolationInfo::from)
                    .collect(Collectors.toList());
        } else if (ex instanceof MethodArgumentNotValidException manve) {
            constraintViolations = manve.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(ConstraintViolationInfo::from)
                    .toList();
        } else {
            handleUnknownException(ex, request);
        }

        ErrorMessage message = new ConstraintViolationErrorMessage(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                "Constraint Violation",
                request.getDescription(false),
                constraintViolations
        );
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "Failed to read request body",
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
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
