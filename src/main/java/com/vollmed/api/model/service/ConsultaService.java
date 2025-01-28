package com.vollmed.api.model.service;

import org.springframework.stereotype.Service;

import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.repository.ConsultaRepository;

/**
 * Serviço para as consultas médicas
 * 
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaController
 */
@Service
public class ConsultaService {
    
    private ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }
}
