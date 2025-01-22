package com.vollmed.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.dto.DadosPacienteCadastrado;
import com.vollmed.api.model.service.PacienteService;

import jakarta.validation.Valid;

/**
 * Controller para os recursos dos pacientes
 * @since branch pacientes
 * @author Jean Maciel
 */
@RestController
@RequestMapping("/paciente")
public class PacienteController {

    private PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    /**
    * Cadastra um paciente no sistema
    * @param dadosDeCadastro que vem no corpo da requisição
    * @param uriBuilder para montar a URI do recurso
    * @return um DTO com os dados do paciente cadastrado
    */
    @PostMapping
    public ResponseEntity<DadosPacienteCadastrado> cadastrarPaciente(
        @RequestBody @Valid DadosCadastroPaciente dadosDeCadastro, UriComponentsBuilder uriBuilder) {
        DadosPacienteCadastrado dadosCadastrados = pacienteService.cadastrarPaciente(dadosDeCadastro);

        URI uri = uriBuilder.path("/{id}").buildAndExpand(dadosCadastrados.id()).toUri();

        return ResponseEntity.created(uri).body(dadosCadastrados);
    }
}
