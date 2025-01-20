package com.vollmed.api.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de atualização de um médico
 * @since branch medicos
 * @author Jean Maciel
 */
public record DadosAtualizacaoMedico(

    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    String nome,

    @Pattern(regexp = "^[0-9]{11}$", message = "O telefone está em formato incorreto, deve conter apenas os 11 dígitos")
    String telefone,

    @Valid
    DadosAtualizacaoEndereco endereco
) {

}
