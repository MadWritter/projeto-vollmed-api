package com.vollmed.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vollmed.api.model.entity.Paciente;

/**
 * Repository para as transações pertinentes aos pacientes
 * @since branch pacientes
 * @author Jean Maciel
 * @see Paciente
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
