package com.vollmed.api.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
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

    /**
     * Classe para a construção de respostas HTTP a respeito das exceções
     */
    private class ExceptionMessageBuilder {
        private LocalDate timestamp;
        private Integer status;
        private String message;
        private String uri;

        /**
         * Construtor para a mensagem a ser devolvida
         * 
         * @param timestamp horário do ocorrido
         * @param status    o status HTTP
         * @param message   a mensagem informando o ocorrido
         * @param uri       o caminho de onde ocorreu a exceção
         */
        private ExceptionMessageBuilder(LocalDate timestamp, Integer status, String message, String uri) {
            this.timestamp = timestamp;
            this.status = status;
            this.message = message;
            this.uri = uri;
        }

        /**
         * Devolve a mensagem organizada.
         * 
         * @return um LinkedHashMap organizado com a mensagem a ser devolvida
         */
        private Map<String, Object> getMessage() {
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("timestamp", this.timestamp);
            message.put("status", this.status);
            message.put("message", this.message);
            message.put("path", this.uri);
            return message;
        }
    }
}
