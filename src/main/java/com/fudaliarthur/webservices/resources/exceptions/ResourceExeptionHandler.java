package com.fudaliarthur.webservices.resources.exceptions;

import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundExeption;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExeptionHandler {
    @ExceptionHandler(ResourceNotFoundExeption.class)
    public ResponseEntity<StandartError> resourceNotFound(ResourceNotFoundExeption e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandartError err = new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
