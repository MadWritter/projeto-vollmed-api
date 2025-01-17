package com.vollmed.api.controller;

import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.service.MedicoService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller para as requisições dos recursos dos médicos
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
    * @param dadosDeCadastro que vem no corpo da requisição
    * @param uriBuilder para construir a URI do recurso criado
    * @return um DTO com os dados do médico cadastrado
    */
    @PostMapping
    public ResponseEntity<DadosMedicoCadastrado> cadastrarMedico(
        @RequestBody @Valid DadosCadastroMedico dadosDeCadastro, UriComponentsBuilder uriBuilder) {

        DadosMedicoCadastrado dadosMedicoCadastrado = medicoService.cadastrarMedico(dadosDeCadastro);
        URI uri = uriBuilder.path("/{id}").buildAndExpand(dadosMedicoCadastrado.id()).toUri();

        return ResponseEntity.created(uri).body(dadosMedicoCadastrado);
    }
}
