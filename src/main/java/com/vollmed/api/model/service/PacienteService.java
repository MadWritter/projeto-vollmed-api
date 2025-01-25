package com.vollmed.api.model.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.vollmed.api.controller.PacienteController;
import com.vollmed.api.model.dto.DadosAtualizacaoPaciente;
import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.dto.DadosPacienteCadastrado;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;
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

    /**
    * Cadastra um paciente no sistema
    * @param dadosCadastroPaciente que vieram na requisiçãi
    * @return um DTO com os dados do paciente cadastrado
    */
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

    /**
    * Retorna os dados de um paciente a partir do Id
    * @param id que veio na URI da requisição
    * @return um DTO com os dados do paciente encontrado
    */
    public DadosPacienteCadastrado findById(Long id) {
        try {
            return pacienteRepository.findByIdAndAtivoTrue(id).map(DadosPacienteCadastrado::new).orElse(null);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao consultar o paciente, o banco está inoperante");
        }
    }

    /**
    * Busca por todos os pacientes cadastrados
    * @param sort atributo de ordenação da consulta
    * @param page a página a ser consultada
    * @return uma página com os dados dos pacientes cadastrados
    */
    public Page<DadosPacienteCadastrado> findAll(String sort, int page) {
        Pageable paginacao = PageRequest.of(page, 10, Sort.by(Direction.ASC, sort));
        try {
            return pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosPacienteCadastrado::new);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao consultar a lista de pacientes, o banco está inoperante");
        }
    }

    /**
    * Atualiza um paciente no sistema
    * @param id que veio na requisição
    * @param dadosDeAtualizacao que vieram no corpo da requisição
    * @return um DTO com os dados atualizados
    */
    public DadosPacienteCadastrado atualizarPaciente(Long id, DadosAtualizacaoPaciente dadosDeAtualizacao) {
        Optional<Paciente> pacienteConsultado = pacienteRepository.findByIdAndAtivoTrue(id);

        if(pacienteConsultado.isEmpty()) {
            throw new EntityNotFoundException("O ID informado não tem um correspondente");
        }
        try {
            pacienteConsultado.get().atualizarDados(dadosDeAtualizacao);
            pacienteRepository.flush();
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao processar a atualização, o banco está inoperante");
        }

        return new DadosPacienteCadastrado(pacienteConsultado.get());
    }

    /**
    * Faz a exclusão de um paciente do sistema
    * @param id que veio na requisição
    * @return um booleano informando sobre o sucesso da transação
    */
    public Boolean excluirPaciente(Long id) {
        Optional<Paciente> pacienteConsultado = pacienteRepository.findByIdAndAtivoTrue(id);

        if(pacienteConsultado.isEmpty()) {
            throw new EntityNotFoundException("O ID informado não tem um correspondente");
        }

        try {
            pacienteConsultado.get().setAtivo(false);
            pacienteRepository.flush();
            return true;
        } catch(PersistenceException e) {
            return false;
        }

    }
}
