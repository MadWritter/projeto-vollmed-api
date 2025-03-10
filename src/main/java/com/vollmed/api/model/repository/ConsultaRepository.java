package com.vollmed.api.model.repository;

import java.time.LocalDateTime;
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

    @Query("SELECT c FROM Consulta c WHERE c.dataDaConsulta = :dataDaConsulta AND c.paciente = :paciente AND c.status = 'AGENDADA'")
    Optional<Consulta> findByDataAndPacienteAndAgendada(@Param("dataDaConsulta") LocalDateTime dataDaConsulta, @Param("paciente") Paciente paciente);

	long countByMedicoAndDataDaConsultaBetween(Medico medico, LocalDateTime inicioConsulta, LocalDateTime finalConsulta);

	@Query("SELECT c FROM Consulta c WHERE c.id = :id AND c.status = 'AGENDADA'")
    Optional<Consulta> findByIdAndStatusAgendada(@Param("id") Long consultaId);
}
