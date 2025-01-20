package com.vollmed.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.model.entity.Especialidade;
import com.vollmed.api.model.entity.Medico;

/**
 * DTO que representa os dados de um médico cadastrado
 * @since branch medicos
 * @author Jean Maciel
 */
public record DadosMedicoCadastrado(
    Long id,
    String nome,
    String email,
    @JsonProperty("CRM")
    String crm,
    Especialidade especialidade
) {
    public DadosMedicoCadastrado(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}
