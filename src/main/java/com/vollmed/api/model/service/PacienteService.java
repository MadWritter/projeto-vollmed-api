package com.vollmed.api.model.service;

import org.springframework.stereotype.Service;

import com.vollmed.api.controller.PacienteController;
import com.vollmed.api.model.repository.PacienteRepository;

/**
 * Serviço para os recursos do paciente
 * @since branch pacientes
 * @author Jean Maciel
 * @see PacienteController
 */
@Service
public class PacienteService {

    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }
}
