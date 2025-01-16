package com.vollmed.api.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa os dados de endereço
 * @since branch medicos
 * @author Jean Maciel
 */
@NoArgsConstructor
@Getter
@Embeddable
public class Endereco {

    @NotBlank(message = "O logradouro não pode estar em branco")
    @Size(min = 4, max = 100, message = "O logradouro deve conter entre 4 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String logradouro;

    @Min(value = 1, message = "O valor mínimo de número é 1")
    @Max(value = 99999, message = "O valor máximo de número e 99999")
    private Integer numero;

    @Size(min = 3, max = 100, message = "O complemento deve conter entre 3 e 100 caracteres")
    @Column(length = 100)
    private String complemento;

    @NotBlank(message = "O bairro deve ser informado")
    @Size(min = 3, max = 50, message = "O bairro deve conter entre 3 e 50 caracteres")
    @Column(nullable = false, length = 50)
    private String bairro;

    @NotBlank(message = "A cidade deve ser informada")
    @Size(min = 4, max = 50, message = "A cidade deve conter entre 4 e 50 caracteres")
    @Column(nullable = false, length = 50)
    private String cidade;

    @NotNull(message = "Deve informar uma das unidades federativas em sigla, ex(SP, RJ...)")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    @JsonProperty("UF")
    private UF uf;

    @NotBlank(message = "Deve informar o cep")
    @Pattern(regexp = "^[0-9]{8}", message = "Deve conter apenas os 8 dígitos do cep")
    @Column(nullable = false, length = 8)
    @JsonProperty("CEP")
    private String cep;

    /**
    * Construtor para essa entidade
    * @param logradouro até 100 dígitos
    * @param numero entre 1 e 99999
    * @param complemento até 100 dígitos
    * @param bairro até 50 dígitos
    * @param cidade até 50 dígitos
    * @param uf uma das unidades federativas
    * @param cep os 6 dígitos
    */
    public Endereco(String logradouro, Integer numero, String complemento, String bairro, String cidade, UF uf, String cep) {
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCidade(cidade);
        setUf(uf);
        setCep(cep);
    }

	public void setLogradouro(String logradouro) {
	   if(logradouro != null && !logradouro.isBlank()) {
	     this.logradouro = logradouro;
	   }
	}

	public void setNumero(Integer numero) {
	   this.numero = numero;
	}

	public void setComplemento(String complemento) {
	   this.complemento = complemento;
	}

	public void setBairro(String bairro) {
		if(bairro != null && !bairro.isBlank()) {
		  this.bairro = bairro;
		}
	}

	public void setCidade(String cidade) {
		if(cidade != null && !cidade.isBlank()) {
		  this.cidade = cidade;
		}
	}

	public void setUf(UF uf) {
		if(uf != null) {
		  this.uf = uf;
		}
	}

	public void setCep(String cep) {
		if(cep != null && !cep.isBlank()) {
		  this.cep = cep;
		}
	}


}
