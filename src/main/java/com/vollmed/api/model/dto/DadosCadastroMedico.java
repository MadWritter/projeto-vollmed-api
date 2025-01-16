package com.vollmed.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.controller.MedicoController;
import com.vollmed.api.model.entity.Endereco;
import com.vollmed.api.model.entity.Especialidade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para receber os dados de cadastro de um médico
 * @since branch medicos
 * @author Jean Maciel
 * @see MedicoController
 */
public record DadosCadastroMedico(

    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    String nome,

    @NotBlank(message = "O email não pode ser vazio")
    @Email(message = "O campo email está em formato incorreto")
    @Size(min = 5, max = 100, message = "O email deve conter entre 5 e 100 caracteres")
    String email,

    @NotBlank(message = "O telefone não pode ser vazio")
    @Pattern(regexp = "^[0-9]{11}$", message = "O telefone está em formato incorreto, deve conter apenas os 11 dígitos")
    String telefone,

    @NotBlank(message = "O crm não pode ser vazio")
    @Pattern(regexp = "^[0-9]{6}", message = "O crm deve conter apenas os 6 dígitos obrigatórios")
    @JsonProperty("CRM")
    String crm,

    @NotNull(message = "A especialidade deve ser informada")
    Especialidade especialidade,

    @NotNull(message = "Deve conter os dados de endereço")
    @Valid
    Endereco endereco

) {

}
