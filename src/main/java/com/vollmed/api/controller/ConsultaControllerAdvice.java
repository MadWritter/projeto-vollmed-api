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

/**
 * Controller Advice para tratar as exceções no ConsultaController
 *
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaController
 */
@RestControllerAdvice(assignableTypes = ConsultaController.class)
public class ConsultaControllerAdvice {

    /**
    * Trata as exceções pertinentes a erros na solicitação do cliente
    *
    * @param ex gerada por erro da solicitação
    * @param req objeto com metadados da requisição
    * @return um 400(Bad Request) informando o erro na solcitação, de acordo com a exceção
    */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 400, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
    * Trata as exceções pertinentes a erros de persistência de dados
    *
    * @param ex gerada por erro na persistência de dados
    * @param req objeto com metadados da requisição
    * @return um 500(Internal Server Error) informando o erro ocorrido ao processar a solicitação
    */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 500, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
    * Trata as exceções sobre entidades que não foram encontradas
    *
    * @param ex gerada ao não encontrar uma entidade nescessária para processar o recurso
    * @param req objeto com metadados da requisição
    * @return um 400(Bad Request) informando o erro na solicitação
    */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 400, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
