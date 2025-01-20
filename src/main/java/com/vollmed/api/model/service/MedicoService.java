package com.vollmed.api.model.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vollmed.api.controller.MedicoController;
import com.vollmed.api.model.dto.DadosAtualizacaoMedico;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.repository.MedicoRepository;

import jakarta.persistence.EntityNotFoundException;
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
    @Transactional(rollbackFor = {PersistenceException.class, DataIntegrityViolationException.class})
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
            return medicoRepository.findByIdAndAtivoTrue(id).map(DadosMedicoCadastrado::new).orElse(null);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao consultar um médico cadastrado, o banco está inoperante");
        }
    }

    /**
    * Busca pela lista de médicos cadastrados
    * @param sort atributo de ordenação da consulta
    * @param page a página a ser consultada
    * @return uma página com os médicos encontrados conforme a ordenação
    */
    @Transactional(readOnly = true)
    public Page<DadosMedicoCadastrado> findAll(String sort, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Direction.ASC,sort));
        try {
            return medicoRepository.findAllByAtivoTrue(pageable).map(DadosMedicoCadastrado::new);
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao consultar a lista de médicos, o banco está inoperante");
        }
    }

    /**
    * Atualiza os dados de um médico cadastrado
    * @param id que vem na URI
    * @param dadosDeAtualizacao que vieram no corpo da requisição
    * @return um DTO com os dados atualizados
    * @throws EntityNotFoundException caso não tenha uma entidade cadastrada
    * com o id informado
    */
    @Transactional(rollbackFor = {DataIntegrityViolationException.class, DataIntegrityViolationException.class})
    public DadosMedicoCadastrado atualizarDados(Long id, DadosAtualizacaoMedico dadosDeAtualizacao) {
        Optional<Medico> medicoCadastrado = medicoRepository.findByIdAndAtivoTrue(id);
        if(medicoCadastrado.isEmpty()) {
            throw new EntityNotFoundException("O ID informado não tem um correspondente");
        }
        try {
            medicoCadastrado.get().atualizarDados(dadosDeAtualizacao);
            medicoRepository.flush();
        } catch(DataIntegrityViolationException e) {
            if(e.getMessage().toLowerCase().contains("telefone nulls first")) {
                throw new IllegalArgumentException("Telefone já cadastrado");
            }
            throw e;
        } catch(PersistenceException e) {
            throw new PersistenceException("Erro ao processar a atualização, o banco está fora");
        }
        return new DadosMedicoCadastrado(medicoCadastrado.get());
    }

    /**
    * Faz a exclusão lógica, desativando o médico do sistemae
    * @param id que vem na requisição
    * @return true caso consiga excluir e false caso haja problema
    * ao sincronizar a atualzação com o banco
    */
    @Transactional(rollbackFor = PersistenceException.class)
    public Boolean excluirMedico(Long id) {
        Optional<Medico> medicoCadastrado = medicoRepository.findByIdAndAtivoTrue(id);
        if(medicoCadastrado.isEmpty()) {
            throw new EntityNotFoundException("O ID informado não tem um correspondente");
        }
        try {
            medicoCadastrado.get().setAtivo(false);
            medicoRepository.flush();
            return true;
        } catch(PersistenceException e) {
            return false;
        }
    }
}
