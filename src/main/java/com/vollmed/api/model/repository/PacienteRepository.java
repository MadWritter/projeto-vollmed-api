package com.vollmed.api.model.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vollmed.api.model.entity.Paciente;

/**
 * Repository para as transações pertinentes aos pacientes
 * 
 * @since branch pacientes
 * @author Jean Maciel
 * @see Paciente
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca por um paciente ativo a partir do ID
     * 
     * @param id para consulta
     * @return um Optional, contendo ou não o paciente
     */
    Optional<Paciente> findByIdAndAtivoTrue(Long id);

    /**
     * Busca por todos os pacientes ativos no sistema
     * 
     * @param paginacao para ordenação e página a ser consultada
     * @return uma página com os resultados encontrados
     */
    Page<Paciente> findAllByAtivoTrue(Pageable paginacao);
}
