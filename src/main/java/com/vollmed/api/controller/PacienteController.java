package com.vollmed.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vollmed.api.model.service.PacienteService;

/**
 * Controller para os recursos dos pacientes
 * @since branch pacientes
 * @author Jean Maciel
 */
@RestController
@RequestMapping("/paciente")
public class PacienteController {

    private PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
}
