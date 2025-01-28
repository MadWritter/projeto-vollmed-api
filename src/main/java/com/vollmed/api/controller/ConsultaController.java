package com.vollmed.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vollmed.api.model.service.ConsultaService;

/**
 * Endpoint para os recursos das consultas médicas
 * 
 * @since branch consultas
 * @author Jean Maciel
 */
@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    
    private ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }
}
