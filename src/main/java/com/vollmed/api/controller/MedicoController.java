package com.vollmed.api.controller;

import com.vollmed.api.model.service.MedicoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para as requisições dos recursos dos médicos
 * @since branch medicos
 * @author Jean Maciel
 * @see MedicoService
 */
@RestController
@RequestMapping("/medico")
public class MedicoController {

    private MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }
}
