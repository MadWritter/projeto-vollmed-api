package com.vollmed.api.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.controller.ConsultaController;
import com.vollmed.api.model.entity.Especialidade;

import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa os dados de cadastro da consulta médica
 * 
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaController
 */
public record DadosCadastroConsulta(

    @NotNull(message = "Precisa informar o ID do paciente")
    @JsonProperty(value = "id_paciente")
    Long pacienteId,

    @NotNull(message = "Precisa informara a especialidade da consulta")
    Especialidade especialidade,

    @NotNull(message = "Deve informar a data e hora da consulta: HH:mm dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    @JsonProperty(value = "data_da_consulta")
    LocalDateTime dataDaConsulta
) {
    
}
