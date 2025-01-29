package com.vollmed.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.service.ConsultaService;

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
}
