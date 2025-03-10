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
 * Controller Advice para tratar as exceções no MedicoController
 *
 * @since branch medicos
 * @author Jean Maciel
 * @see MedicoController
 */
@RestControllerAdvice(assignableTypes = MedicoController.class)
public class MedicoControllerAdvice {

    /**
     * Trata as exceções de validação de dados unique
     *
     * @param ex  gerada pelo serviço
     * @param req dados da requisição enviada
     * @return um HTTP 400 com uma mensagem informando onde a validação falhou
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 400, ex.getMessage(), req.getRequestURI());
        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata as exceções de persistência de dados
     *
     * @param ex  gerada na tentativa de persistir os dados
     * @param req dados da requisição enviada
     * @return um HTTP 500 com uma resposta sobre o ocorrido
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 500,
                "Erro ao processar a solicitação, tente novamente em instantes", req.getRequestURI());
        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Trata as exceções de quando entidades não são encontrada
     *
     * @param ex  gerada na busca pela entidade
     * @param req dados da requisição enviada
     * @return um HTTP 404 com uma resposta sobre o ocorrido
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest req) {
        var messageBuilder = new ExceptionMessageBuilder(LocalDate.now(), 404, ex.getMessage(), req.getRequestURI());

        Map<String, Object> res = messageBuilder.getMessage();

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
}
