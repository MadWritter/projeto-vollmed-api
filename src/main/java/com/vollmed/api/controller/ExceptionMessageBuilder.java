package com.vollmed.api.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder para as mensagens de exceção dos Controllers
 *
 * @since branch consultas
 * @author Jean Maciel
 */
class ExceptionMessageBuilder {
    private LocalDate timestamp;
    private Integer status;
    private String message;
    private String uri;

    /**
    * Constrói uma mensagem para ser devolvida para o cliente
    * @param timestamp data e hora do ocorrido
    * @param status código http
    * @param message a mensagem a ser devolvida
    * @param uri onde ocorreu o problema com o recurso
    */
    ExceptionMessageBuilder(LocalDate timestamp, Integer status, String message, String uri) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.uri = uri;
    }

    /**
    * Obtém a mensagem criada
    * @return um LinkedHashMap com a mensagem criada
    */
    Map<String, Object> getMessage() {
        Map<String, Object> message = new LinkedHashMap<>();
        message.put("timestamp", this.timestamp);
        message.put("status", this.status);
        message.put("message", this.message);
        message.put("path", this.uri);
        return message;
    }
}
