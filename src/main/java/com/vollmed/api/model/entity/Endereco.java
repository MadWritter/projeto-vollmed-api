package com.vollmed.api.model.entity;

import com.vollmed.api.model.dto.DadosCadastroEndereco;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa os dados de endereço
 * 
 * @since branch medicos
 * @author Jean Maciel
 */
@NoArgsConstructor
@Getter
@Embeddable
public class Endereco {

  @Column(nullable = false, length = 100)
  private String logradouro;

  private Integer numero;

  @Column(length = 100)
  private String complemento;

  @Column(nullable = false, length = 50)
  private String bairro;

  @Column(nullable = false, length = 50)
  private String cidade;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 2)
  private UF uf;

  @Column(nullable = false, length = 8)
  private String cep;

  /**
   * Construtor para essa entidade
   * 
   * @param logradouro  até 100 dígitos
   * @param numero      entre 1 e 99999
   * @param complemento até 100 dígitos
   * @param bairro      até 50 dígitos
   * @param cidade      até 50 dígitos
   * @param uf          uma das unidades federativas
   * @param cep         os 6 dígitos
   */
  public Endereco(String logradouro, Integer numero, String complemento, String bairro, String cidade, UF uf,
      String cep) {
    setLogradouro(logradouro);
    setNumero(numero);
    setComplemento(complemento);
    setBairro(bairro);
    setCidade(cidade);
    setUf(uf);
    setCep(cep);
  }

  public Endereco(DadosCadastroEndereco dados) {
    this(dados.logradouro(), dados.numero(), dados.complemento(), dados.bairro(), dados.cidade(), dados.uf(),
        dados.cep());
  }

  public void setLogradouro(String logradouro) {
    if (logradouro != null && !logradouro.isBlank()) {
      this.logradouro = logradouro;
    }
  }

  public void setNumero(Integer numero) {
    if (numero != null && numero > 0) {
      this.numero = numero;
    }
  }

  public void setComplemento(String complemento) {
    if (complemento != null && !complemento.isBlank()) {
      this.complemento = complemento;
    }
  }

  public void setBairro(String bairro) {
    if (bairro != null && !bairro.isBlank()) {
      this.bairro = bairro;
    }
  }

  public void setCidade(String cidade) {
    if (cidade != null && !cidade.isBlank()) {
      this.cidade = cidade;
    }
  }

  public void setUf(UF uf) {
    if (uf != null) {
      this.uf = uf;
    }
  }

  public void setCep(String cep) {
    if (cep != null && !cep.isBlank()) {
      this.cep = cep;
    }
  }
}
