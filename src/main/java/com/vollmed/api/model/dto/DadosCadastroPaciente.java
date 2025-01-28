package com.vollmed.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.controller.PacienteController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que recebe os dados de cadastro do paciente
 * 
 * @since branch pacientes
 * @author Jean Maciel
 * @see PacienteController
 */
public record DadosCadastroPaciente(

    @NotBlank(message = "Deve conter o nome do paciente")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    String nome,

    @NotBlank(message = "Deve conter o email do paciente")
    @Email(message = "O formato do email está incorreto")
    @Size(min = 4, max = 100, message = "O email deve conter entre 4 e 100 caracteres")
    String email,

    @NotBlank(message = "Deve conter o telefone do paciente")
    @Pattern(regexp = "^[0-9]{11}", message = "O telefone deve conter os 11 dígitos, DDD(2) + Número(9)")
    String telefone,

    @NotBlank(message = "Deve conter o CPF do paciente")
    @Pattern(regexp = "^[0-9]{11}", message = "O CPF deve conter apenas os 11 dígitos")
    @JsonProperty(value = "CPF")
    String cpf,

    @NotNull(message = "Os dados de endereço são obrigatórios")
    @Valid
    DadosCadastroEndereco endereco
) {

}
