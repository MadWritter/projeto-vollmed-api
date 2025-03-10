package com.vollmed.api.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = ConsultaController.class)
public class ConsultaControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 400, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 500, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 400, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
