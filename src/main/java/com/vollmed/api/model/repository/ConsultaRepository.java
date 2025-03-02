package com.vollmed.api.model.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vollmed.api.model.entity.Consulta;
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

    @Query("SELECT c FROM Consulta c WHERE c.dataDaConsulta = :data AND c.paciente = :paciente AND c.status = 'AGENDADA'")
    Optional<Consulta> findByDataAndPacienteAndAgendada(LocalDateTime dataDaConsulta, Paciente paciente);
}
