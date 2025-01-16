package com.vollmed.api.model.service;

import com.vollmed.api.controller.MedicoController;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.repository.MedicoRepository;

import jakarta.persistence.PersistenceException;

import org.springframework.dao.DataIntegrityViolationException;
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

    /**
    * Realiza o cadastro de um médico
    * @param dadosDeCadastro que vieram na requisição
    * @return um DTO com os dados do médico cadastrado
    */
    public DadosMedicoCadastrado cadastrarMedico(DadosCadastroMedico dadosDeCadastro) {
        var medicoParaCadastrar = new Medico(dadosDeCadastro);
        try {
            Medico medicoCadastrado = medicoRepository.save(medicoParaCadastrar);
            return new DadosMedicoCadastrado(medicoCadastrado);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao cadastrar o médico, o banco está inoperante");
        } catch (DataIntegrityViolationException e) {
            if(e.getMessage().toLowerCase().contains("email nulls first")) {
                throw new IllegalArgumentException("Email já cadastrado");
            }

            if(e.getMessage().toLowerCase().contains("telefone nulls first")) {
                throw new IllegalArgumentException("Telefone já cadastrado");
            }

            if(e.getMessage().toLowerCase().contains("crm nulls first")) {
                throw new IllegalArgumentException("CRM já cadastrado");
            }
            throw e;
        }
    }
}
