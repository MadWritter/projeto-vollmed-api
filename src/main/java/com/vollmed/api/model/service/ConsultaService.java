package com.vollmed.api.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.ConsultaRepository;
import com.vollmed.api.model.repository.MedicoRepository;
import com.vollmed.api.model.repository.PacienteRepository;

/**
 * Serviço para as consultas médicas
 *
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaController
 */
@Service
public class ConsultaService {

    private ConsultaRepository consultaRepository;
    private PacienteRepository pacienteRepository;
    private MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
        PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    /**
    * Faz o cadastro de uma consulta médica
    *
    * @param dadosDeCadastro que vieram na requisição
    * @return um DTO com os dados da consulta cadastrada.
    */
    public DadosConsultaCadastrada cadastrarConsulta(DadosCadastroConsulta dadosDeCadastro) {
        Optional<Paciente> pacienteConsultado = pacienteRepository.findByIdAndAtivoTrue(dadosDeCadastro.pacienteId());
        List<Medico> medicosPorEspecialidade = medicoRepository.findAllByEspecialidadeAndAtivoTrue(dadosDeCadastro.especialidade());

        Consulta consultaParaCadastrar = new Consulta(pacienteConsultado.get(), medicosPorEspecialidade.get(0), dadosDeCadastro.dataDaConsulta());

        Consulta consultaCadastrada = consultaRepository.save(consultaParaCadastrar);

        return new DadosConsultaCadastrada(consultaCadastrada);
    }

}
