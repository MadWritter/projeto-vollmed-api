package com.vollmed.api.model.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.vollmed.api.controller.PacienteController;
import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.dto.DadosPacienteCadastrado;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.PacienteRepository;

import jakarta.persistence.PersistenceException;

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

    public DadosPacienteCadastrado cadastrarPaciente(DadosCadastroPaciente dadosCadastroPaciente) {
        var pacienteParaCadastrar = new Paciente(dadosCadastroPaciente);
        try {
            var pacienteCadastrado = pacienteRepository.save(pacienteParaCadastrar);
            return new DadosPacienteCadastrado(pacienteCadastrado);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao cadastrar o paciente, o banco está inoperante");
        } catch(DataIntegrityViolationException e) {
            if(e.getMessage().toLowerCase().contains("email nulls first")) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            if(e.getMessage().toLowerCase().contains("telefone nulls first")) {
                throw new IllegalArgumentException("Telefone já cadastrado");
            }
            if(e.getMessage().toLowerCase().contains("cpf nulls first")) {
                throw new IllegalArgumentException("CPF já cadastrado");
            }
            throw e;
        }
    }

    public DadosPacienteCadastrado findById(Long id) {
        try {
            return pacienteRepository.findById(id).map(DadosPacienteCadastrado::new).orElse(null);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao consultar o paciente, o banco está inoperante");
        }
    }
}
