package com.vollmed.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.model.entity.Paciente;

/**
 * DTO que representa os dados de um paciente cadastrado
 * @since branch pacientes
 * @author Jean Maciel
 */
public record DadosPacienteCadastrado(
    Long id,
    String nome,
    String email,
    @JsonProperty(value = "CPF")
    String cpf
) {
    public DadosPacienteCadastrado(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }
}
