package com.vollmed.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosCancelamentoConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.service.ConsultaService;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

/**
 * Endpoint para os recursos das consultas médicas
 *
 * @since branch consultas
 * @author Jean Maciel
 */
@RestController
@RequestMapping("/consulta")
public class ConsultaController {

    private ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    /**
    * Efetua o cadastro de uma consulta médica
    *
    * @param dadosDeCadastro que vem no corpo da requisição
    * @param uriBuilder para construir a URI do recurso criado
    * @return um DTO com os dados da consulta cadastrada
    */
    @PostMapping
    public ResponseEntity<DadosConsultaCadastrada> cadastrarConsulta(
        @RequestBody @Valid DadosCadastroConsulta dadosDeCadastro, UriComponentsBuilder uriBuilder) {
            DadosConsultaCadastrada dadosCadastrados = consultaService.cadastrarConsulta(dadosDeCadastro);

            URI uri = uriBuilder.path("/{id}").buildAndExpand(dadosCadastrados.idConsulta()).toUri();

            return ResponseEntity.created(uri).body(dadosCadastrados);
    }

    /**
    * Finaliza uma consulta cadastrada
    *
    * @param id da consulta que deseja finalizar
    * @return um 204(No Content) indicando sucesso ao finalizar a consulta
    */
    @PutMapping("/{id}")
    public ResponseEntity<Object> finalizarConsulta(@PathVariable Long id) {
        boolean finalizou = consultaService.finalizarConsulta(id);

        if(!finalizou) {
            throw new PersistenceException("Erro ao finalizar a consulta, tente novamente em instantes");
        }

        return ResponseEntity.noContent().build();
    }

    /**
    * Faz a exclusão lógica de uma consulta cadastrada
    *
    * @param id da consulta que deseja cancelar
    * @param dadosDeCancelamento informando o motivo e a observação do cancelamento
    * @return um 204(No Content) indicando sucesso ao cancelar a consulta
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancelarConsulta(
        @PathVariable Long id, @RequestBody @Valid DadosCancelamentoConsulta dadosDeCancelamento) {

        boolean cancelou = consultaService.cancelarConsulta(id, dadosDeCancelamento);

        if(!cancelou) {
            throw new PersistenceException("Erro ao efetuar o cancelamento da consulta, tente novamente em instantes");
        }

        return ResponseEntity.noContent().build();
    }
}
