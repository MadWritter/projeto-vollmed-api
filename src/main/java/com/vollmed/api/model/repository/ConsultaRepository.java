package com.vollmed.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vollmed.api.model.entity.Consulta;
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
    
}
