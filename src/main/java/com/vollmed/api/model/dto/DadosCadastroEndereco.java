package com.vollmed.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vollmed.api.model.entity.UF;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que representa os dados de endereço no cadastro
 * 
 * @since branch medicos
 * @author Jean Maciel
 * @see DadosCadastroMedico
 */
public record DadosCadastroEndereco(

    @NotBlank(message = "O logradouro não pode estar em branco")
    @Size(min = 4, max = 100, message = "O logradouro deve conter entre 4 e 100 caracteres")
    String logradouro,

    @Min(value = 1, message = "O valor mínimo de número é 1")
    @Max(value = 99999, message = "O valor máximo de número e 99999")
    Integer numero,

    @Size(min = 3, max = 100, message = "O complemento deve conter entre 3 e 100 caracteres")
    String complemento,

    @NotBlank(message = "O bairro deve ser informado")
    @Size(min = 3, max = 50, message = "O bairro deve conter entre 3 e 50 caracteres")
    String bairro,

    @NotBlank(message = "A cidade deve ser informada")
    @Size(min = 4, max = 50, message = "A cidade deve conter entre 4 e 50 caracteres")
    String cidade,

    @NotNull(message = "Deve informar uma das unidades federativas em sigla, ex(SP, RJ...)")
    @JsonProperty("UF")
    UF uf,

    @NotBlank(message = "Deve informar o cep")
    @Pattern(regexp = "^[0-9]{8}", message = "Deve conter apenas os 8 dígitos do cep")
    @JsonProperty("CEP")
    String cep
) {

}
