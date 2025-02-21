package com.vollmed.api.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.component.ClockConfig;
import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.ConsultaRepository;
import com.vollmed.api.model.repository.MedicoRepository;
import com.vollmed.api.model.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço para as consultas médicas
 *
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaController
 */
@Service
public class ConsultaService {

    private ClockConfig clockConfig;
    private ConsultaRepository consultaRepository;
    private PacienteRepository pacienteRepository;
    private MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
        PacienteRepository pacienteRepository, MedicoRepository medicoRepository,
        ClockConfig clockConfig) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.clockConfig = clockConfig;
    }

    /**
    * Faz o cadastro de uma consulta médica
    *
    * @param dadosDeCadastro que vieram na requisição
    * @return um DTO com os dados da consulta cadastrada.
    */
    public DadosConsultaCadastrada cadastrarConsulta(DadosCadastroConsulta dadosDeCadastro) {
        validarHoraDaConsulta(dadosDeCadastro.dataDaConsulta());
        Optional<Paciente> pacienteConsultado = pacienteRepository.findByIdAndAtivoTrue(dadosDeCadastro.pacienteId());

        if(pacienteConsultado.isEmpty()) {
            throw new EntityNotFoundException("Paciente informado não foi encontrado");
        }

        List<Medico> medicosPorEspecialidade = medicoRepository.findAllByEspecialidadeAndAtivoTrue(dadosDeCadastro.especialidade());

        if(medicosPorEspecialidade.isEmpty()) {
            throw new EntityNotFoundException("Nenhum médico com a especialidade informada disponível");
        }

        Consulta consultaParaCadastrar = new Consulta(pacienteConsultado.get(), medicosPorEspecialidade.get(0), dadosDeCadastro.dataDaConsulta());

        Consulta consultaCadastrada = consultaRepository.save(consultaParaCadastrar);

        return new DadosConsultaCadastrada(consultaCadastrada);
    }

    /**
    * Método para validar a data e hora da consulta informada
    *
    * @param dataEHora que vem na requisição
    */
	private void validarHoraDaConsulta(LocalDateTime dataEHora) {
	    if(dataEHora.isBefore(clockConfig.now())) {
		  throw new IllegalArgumentException("A data informada é anterior a data atual");
		}

		if(dataEHora.getHour() < 7 || dataEHora.getHour() > 19) {
		  throw new IllegalArgumentException("As consultas devem ser agendadas entre 7h e 19h");
		}
	}

}
