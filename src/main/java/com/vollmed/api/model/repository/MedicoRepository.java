package com.vollmed.api.model.repository;

import com.vollmed.api.model.entity.Medico;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para as transações da entidade Médico
 * 
 * @since branch medicos
 * @author Jean Maciel
 * @see Medico
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * Busca por um Médico que esteja ativo no sistema
     * 
     * @param id para consulta
     * @return um Optional com o possível médico
     */
    Optional<Medico> findByIdAndAtivoTrue(Long id);

    /**
     * Busca todos os médicos ativos no sistema
     * 
     * @param pageable paginação da consulta
     * @return uma página com os dados consultados.
     */
    Page<Medico> findAllByAtivoTrue(Pageable pageable);
}
