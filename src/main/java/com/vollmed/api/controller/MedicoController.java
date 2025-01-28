package com.vollmed.api.controller;

import com.vollmed.api.model.dto.DadosAtualizacaoMedico;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.service.MedicoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller para as requisições dos recursos dos médicos
 * 
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

    /**
     * Solicita o cadastro de um médico
     * 
     * @param dadosDeCadastro que vem no corpo da requisição
     * @param uriBuilder      para construir a URI do recurso criado
     * @return um DTO com os dados do médico cadastrado
     */
    @PostMapping
    public ResponseEntity<DadosMedicoCadastrado> cadastrarMedico(
            @RequestBody @Valid DadosCadastroMedico dadosDeCadastro, UriComponentsBuilder uriBuilder) {

        DadosMedicoCadastrado dadosMedicoCadastrado = medicoService.cadastrarMedico(dadosDeCadastro);
        URI uri = uriBuilder.path("/{id}").buildAndExpand(dadosMedicoCadastrado.id()).toUri();

        return ResponseEntity.created(uri).body(dadosMedicoCadastrado);
    }

    /**
     * Busca os dados de um médico cadastrado
     * 
     * @param id que vem na URL
     * @return um DTO com os dados do médico cadastrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<DadosMedicoCadastrado> findById(@PathVariable Long id) {
        DadosMedicoCadastrado dadosMedicoCadastrado = medicoService.findById(id);

        if (dadosMedicoCadastrado == null) {
            throw new EntityNotFoundException("O ID informado não tem um correspondente");
        }

        return ResponseEntity.ok(dadosMedicoCadastrado);
    }

    /**
     * Busca todos os médicos cadastrados
     * 
     * @param sort atributo de ordenação da consulta
     * @param page a página que deseja buscar
     * @return uma página com os DTO's dos dados encontrados
     */
    @GetMapping
    public ResponseEntity<Page<DadosMedicoCadastrado>> findAll(
            @RequestParam(defaultValue = "nome") String sort, @RequestParam(defaultValue = "0") int page) {
        Page<DadosMedicoCadastrado> pagina = medicoService.findAll(sort, page);
        return ResponseEntity.ok(pagina);
    }

    /**
     * Atualiza os dados de um médico a partir de um id
     * 
     * @param id                 que vem na URI
     * @param dadosDeAtualizacao que vem no corpo da requisição
     * @return um DTO com os dados atualizados
     */
    @PutMapping("/{id}")
    public ResponseEntity<DadosMedicoCadastrado> atualizarMedico(
            @PathVariable Long id, @RequestBody @Valid DadosAtualizacaoMedico dadosDeAtualizacao) {
        DadosMedicoCadastrado dadosAtualizados = medicoService.atualizarDados(id, dadosDeAtualizacao);
        return ResponseEntity.ok(dadosAtualizados);
    }

    /**
     * Exclui um médico do sistema
     * 
     * @param id que vem na URI
     * @return um 204 NO CONTENT caso consiga excluir
     * @throws PersistenceException caso haja problema na sincronização
     *                              com o banco ao excluir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirMedico(@PathVariable Long id) {
        Boolean excluiu = medicoService.excluirMedico(id);

        if (!excluiu) {
            throw new PersistenceException("Erro ao processar a exclusão do médico");
        }

        return ResponseEntity.noContent().build();
    }
}
