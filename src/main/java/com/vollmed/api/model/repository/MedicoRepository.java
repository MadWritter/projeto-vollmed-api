package com.vollmed.api.model.repository;

import com.vollmed.api.model.entity.Especialidade;
import com.vollmed.api.model.entity.Medico;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Busca por todos os médicos ativos com a especialidade informada
     * 
     * @param especialidade a especialidade médica que deverá buscar
     * @return uma lista com os médicos ativos encontrados com essa especialidade
     */
    @Query("SELECT m FROM Medico m WHERE m.especialidade = :especialidade AND m.ativo = true")
    List<Medico> findAllByEspecialidadeAndAtivoTrue(@Param("especialidade") Especialidade especialidade);
}
