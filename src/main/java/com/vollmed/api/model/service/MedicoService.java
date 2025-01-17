package com.vollmed.api.model.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vollmed.api.controller.MedicoController;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.repository.MedicoRepository;

import jakarta.persistence.PersistenceException;

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

    /**
    * Busca pelos dados de um médico cadastrado
    * @param id que vem na requisição
    * @return um DTO com os dados do médico cadastrado
    */
    @Transactional(readOnly = true)
    public DadosMedicoCadastrado findById(Long id) {
        try {
            return medicoRepository.findById(id).map(DadosMedicoCadastrado::new).orElse(null);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao consultar um médico cadastrado, o banco está inoperante");
        }
    }
}
