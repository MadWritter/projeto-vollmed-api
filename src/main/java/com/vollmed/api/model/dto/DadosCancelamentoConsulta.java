package com.vollmed.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.entity.MotivoCancelamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de cancelamento de uma Consulta cadastrada
 *
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaController
 */
public record DadosCancelamentoConsulta(

    @NotNull(message = "O motivo do cancelamento deve ser informado (PACIENTE_DESISTIU, MEDICO_CANCELOU ou OUTROS)")
    @JsonProperty("motivo_do_cancelamento")
    MotivoCancelamento motivoDoCancelamento,

    @Size(min = 5, max = 100, message = "A observação deve conter entre 5 em 100 caracteres")
    String observacao
) {

}
