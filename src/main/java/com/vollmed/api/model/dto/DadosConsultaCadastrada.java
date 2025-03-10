package com.vollmed.api.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.model.entity.Consulta;

/**
 * Representa os dados de uma consulta médica cadastrada
 * 
 * @since branch consultas
 * @author Jean Maciel
 */
public record DadosConsultaCadastrada(

    @JsonProperty(value = "id_consulta")
    Long idConsulta,

    @JsonProperty(value = "nome_paciente")
    String nomePaciente,

    @JsonProperty(value = "nome_medico")
    String nomeMedico,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    @JsonProperty(value = "data_da_consulta")
    LocalDateTime dataDaConsulta
) {
    
    public DadosConsultaCadastrada(Consulta consulta) {
        this(consulta.getId(), consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getDataDaConsulta());
    }
}
