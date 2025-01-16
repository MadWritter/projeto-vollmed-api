package com.vollmed.api.model.repository;

import com.vollmed.api.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para as transações da entidade Médico
 * @since branch medicos
 * @author Jean Maciel
 * @see Medico
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {}
