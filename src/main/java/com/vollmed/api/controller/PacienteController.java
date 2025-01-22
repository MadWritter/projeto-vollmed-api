package com.vollmed.api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.dto.DadosPacienteCadastrado;
import com.vollmed.api.model.service.PacienteService;

import jakarta.persistence.EntityNotFoundException;
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

    /**
    * Retorna um paciente a partir de um Id
    * @param id que vem na URI
    * @return um DTO com os dados do paciente cadastrado
    * @throws EntityNotFoundException que será resolvido pelo controller advice
    */
    @GetMapping("/{id}")
    public ResponseEntity<DadosPacienteCadastrado> findById(@PathVariable Long id) {
        DadosPacienteCadastrado dadosCadastrados = pacienteService.findById(id);

        if(dadosCadastrados == null) {
            throw new EntityNotFoundException("O ID informado não tem um correspondente");
        }

        return ResponseEntity.ok(dadosCadastrados);
    }

    /**
    * Retorna uma página com os médicos cadastrados
    * @param sort atributo de ordenação
    * @param page a página a ser consultada
    * @return uma página com os dados dos médicos cadastrados
    */
    @GetMapping
    public ResponseEntity<Page<DadosPacienteCadastrado>> findAll(
        @RequestParam(defaultValue = "nome") String sort, @RequestParam(defaultValue = "0") int page) {
            Page<DadosPacienteCadastrado> pagina = pacienteService.findAll(sort, page);
            return ResponseEntity.ok(pagina);
    }
}
