package com.vollmed.api.model.service;

import com.vollmed.api.controller.MedicoController;
import com.vollmed.api.model.repository.MedicoRepository;
import org.springframework.stereotype.Service;

/**
 * Serviço para as os recursos dos médicos
 * @since branch medicos
 * @author Jean Maciel
 * @see MedicoController
 */
@Service
public class MedicoService {

    private MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }
}
