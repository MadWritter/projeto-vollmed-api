package com.vollmed.api.model.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.service.ConsultaService;

/**
 * Repository para as transações das consultas médicas
 *
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaService
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>{

    /**
    * Busca uma consulta por data, paciente e status 'AGENDADA'
    *
    * @param dataDaConsulta data para a consulta
    * @param paciente o paciente que irá se consultar
    * @return um Optional com o resultado obtido
    */
    @Query("SELECT c FROM Consulta c WHERE c.dataDaConsulta = :dataDaConsulta AND c.paciente = :paciente AND c.status = 'AGENDADA'")
    Optional<Consulta> findByDataAndPacienteAndAgendada(@Param("dataDaConsulta") LocalDateTime dataDaConsulta, @Param("paciente") Paciente paciente);

    /**
    * Busca uma consulta por ID e status 'AGENDADA'
    *
    * @param consultaId o id da consulta
    * @return um Optional com o resultado obtido
    */
	@Query("SELECT c FROM Consulta c WHERE c.id = :id AND c.status = 'AGENDADA'")
    Optional<Consulta> findByIdAndStatusAgendada(@Param("id") Long consultaId);

    /**
    * Busca uma consulta por médico, data da consulta entre 2 períodos e status 'AGENDADA'
    *
    * @param medico o médico de referência
    * @param inicioConsulta o horário inicial de referência
    * @param finalConsulta o horário final de referência
    * @return uma lista com as consultas agendadas neste período
    */
    @Query("SELECT c FROM Consulta c WHERE c.medico = :medico AND c.status = 'AGENDADA' AND c.dataDaConsulta BETWEEN :inicioConsulta AND :finalConsulta")
    List<Consulta> findByMedicoAndDataDaConsultaBetweenAndAgendada(Medico medico, LocalDateTime inicioConsulta,LocalDateTime finalConsulta);
}
