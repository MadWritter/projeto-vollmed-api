package com.vollmed.api.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.component.ClockConfig;
import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.entity.Status;
import com.vollmed.api.model.repository.ConsultaRepository;
import com.vollmed.api.model.repository.MedicoRepository;
import com.vollmed.api.model.repository.PacienteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;

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
    @Transactional
    public DadosConsultaCadastrada cadastrarConsulta(DadosCadastroConsulta dadosDeCadastro) {
        validarHoraDaConsulta(dadosDeCadastro.dataDaConsulta());
        Optional<Paciente> pacienteConsultado = pacienteRepository.findByIdAndAtivoTrue(dadosDeCadastro.pacienteId());

        if(pacienteConsultado.isEmpty()) {
            throw new EntityNotFoundException("Paciente informado não foi encontrado");
        }

        Optional<Consulta> consultaExistenteNaDataInformada =
                    consultaRepository.findByDataAndPacienteAndAgendada(dadosDeCadastro.dataDaConsulta(), pacienteConsultado.get());
        if(consultaExistenteNaDataInformada.isPresent()) {
            throw new IllegalArgumentException("O paciente informado já tem uma consulta cadastrada nesta data");
        }

        List<Medico> medicosPorEspecialidade = medicoRepository.findAllByEspecialidadeAndAtivoTrue(dadosDeCadastro.especialidade());

        if(medicosPorEspecialidade.isEmpty()) {
            throw new EntityNotFoundException("Nenhum médico com a especialidade informada disponível");
        }

        LocalDateTime inicioConsulta = dadosDeCadastro.dataDaConsulta();
        LocalDateTime finalConsulta = inicioConsulta.plusHours(1);

        Optional<Medico> medicoDisponivel = medicosPorEspecialidade.stream()
            .filter(medico -> consultaRepository.countByMedicoAndDataDaConsultaBetween(medico, inicioConsulta, finalConsulta) == 0)
            .findFirst();

        if(medicoDisponivel.isEmpty()) {
            throw new EntityNotFoundException("Nenhum médico disponível no horário informado");
        }

        Consulta consultaParaCadastrar = new Consulta(pacienteConsultado.get(), medicosPorEspecialidade.get(0), dadosDeCadastro.dataDaConsulta());

        Consulta consultaCadastrada = consultaRepository.save(consultaParaCadastrar);

        return new DadosConsultaCadastrada(consultaCadastrada);
    }

    @Transactional
	public boolean finalizarConsulta(Long id) {
	    Optional<Consulta> consultaAgendada = consultaRepository.findByIdAndStatusAgendada(id);

		if(consultaAgendada.isEmpty()) {
		    throw new IllegalArgumentException("Nenhuma consulta agendada com o ID informado");
		}

		try {
		    consultaAgendada.get().setStatus(Status.CONCLUIDA);
			consultaRepository.flush();
			return true;
		} catch(PersistenceException e) {
		    return false;
		}
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
