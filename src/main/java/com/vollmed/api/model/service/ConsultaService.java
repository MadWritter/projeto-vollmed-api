package com.vollmed.api.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.component.ClockConfig;
import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosCancelamentoConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.MotivoCancelamento;
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
    @Transactional(rollbackFor = PersistenceException.class)
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
            .filter(medico -> {
                List<Consulta> consultasNoIntervalo = consultaRepository.findByMedicoAndDataDaConsultaBetweenAndAgendada(
                    medico,
                    inicioConsulta.minusMinutes(60),
                    finalConsulta.plusMinutes(60)
                );

                return consultasNoIntervalo.isEmpty();
            }
            )
            .findFirst();

        if(medicoDisponivel.isEmpty()) {
            throw new EntityNotFoundException("Nenhum médico disponível no horário informado");
        }

        Consulta consultaParaCadastrar = new Consulta(pacienteConsultado.get(), medicosPorEspecialidade.get(0), dadosDeCadastro.dataDaConsulta());

        Consulta consultaCadastrada = consultaRepository.save(consultaParaCadastrar);

        return new DadosConsultaCadastrada(consultaCadastrada);
    }

    /**
    * Finaliza uma consulta cadastrada
    *
    * @param id que vem na requisição
    * @return um booleano informando o sucesso ao processar a solicitação
    */
    @Transactional(rollbackFor = PersistenceException.class)
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
	* Faz o cancelamento de uma consulta cadastrada
	*
	* @param id que vem na requisição
	* @param dados contendo informações sobre o motivo do cancelamento
	* @return um booleano informando o sucesso ao processar a solicitação
	*/
	@Transactional(rollbackFor = PersistenceException.class)
	public boolean cancelarConsulta(Long id, DadosCancelamentoConsulta dados) {
	    if(dados.motivoDoCancelamento() == MotivoCancelamento.OUTROS &&
		  (dados.observacao() == null || dados.observacao().isEmpty())) {
			throw new IllegalArgumentException("Deve informar uma observação do cancelamento no motivo 'OUTROS'");
	    }


	    Optional<Consulta> consultaCadastrada = consultaRepository.findByIdAndStatusAgendada(id);

		if(consultaCadastrada.isEmpty()) {
		    throw new IllegalArgumentException("Nenhuma consulta agendada com o ID informado");
		}

		validarHoraCancelamento(consultaCadastrada.get());

		try {
    		consultaCadastrada.get().setStatus(Status.CANCELADA);
    		consultaCadastrada.get().setMotivoCancelamento(dados.motivoDoCancelamento());
    		consultaCadastrada.get().setObservacaoCancelamento(dados.observacao());
            consultaRepository.flush();
            return true;
		} catch(PersistenceException e) {
		    return false;
		}
	}

	/**
	* Método para validar o horário do cancelamento
	* Uma consulta não pode ser cancelada com menos de 24h
	*
	* @param consulta com horário para validar
	*/
    private void validarHoraCancelamento(Consulta consulta) {
		LocalDateTime horaCadastradaConsulta = consulta.getDataDaConsulta();
		LocalDateTime dataEhoraAtual = clockConfig.now();

		// se a consulta for em menos de 24h
		// então não é mais possível cancelar
		if(dataEhoraAtual.plusHours(24).isAfter(horaCadastradaConsulta)) {
		    throw new IllegalArgumentException("Você só pode cancelar uma consulta com 24h de antecedência");
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
