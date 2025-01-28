package com.vollmed.api.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de atualização do paciente
 * 
 * @since branch pacientes
 * @author Jean Maciel
 * @see DadosAtualizacaoEndereco
 */
public record DadosAtualizacaoPaciente(

    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    String nome,

    @Pattern(regexp = "^[0-9]{11}", message = "O telefone deve conter os 11 dígitos, DDD(2) + Número(9)")
    String telefone,

    @Valid
    DadosAtualizacaoEndereco endereco
) {

}
