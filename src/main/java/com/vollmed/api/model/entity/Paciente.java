package com.vollmed.api.model.entity;

import com.vollmed.api.model.dto.DadosAtualizacaoPaciente;
import com.vollmed.api.model.dto.DadosCadastroEndereco;
import com.vollmed.api.model.dto.DadosCadastroPaciente;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um Paciente
 * @since branch pacientes
 * @author Jean Maciel
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 11, unique = true)
    private String telefone;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Embedded
    private Endereco endereco;

    /**
    * Construtor para essa entidade
    * @param nome até 100 dígitos
    * @param email até 100 dígitos
    * @param telefone os 11 dígitos
    * @param cpf os 11 dígitos
    * @param endereco os dados para cadastrar o endereço
    */
    public Paciente(String nome, String email, String telefone, String cpf, DadosCadastroEndereco endereco) {
        setNome(nome);
        setEmail(email);
        setTelefone(telefone);
        setCpf(cpf);
        setEndereco(endereco);
    }

    public Paciente(DadosCadastroPaciente dados) {
        this(dados.nome(), dados.email(), dados.telefone(), dados.cpf(), dados.endereco());
    }

	public void setNome(String nome) {
	   if(nome != null && !nome.isBlank()) {
		 this.nome = nome;
	   }
	}

	public void setEmail(String email) {
	   if(email != null && !email.isBlank()) {
		 this.email = email;
	   }
	}

	public void setTelefone(String telefone) {
	   if(telefone != null && !telefone.isBlank()) {
		  this.telefone = telefone;
	   }
	}

	public void setCpf(String cpf) {
	   if(cpf != null && !cpf.isBlank()) {
		 this.cpf = cpf;
	   }
	}

	public void setEndereco(DadosCadastroEndereco endereco) {
		this.endereco = new Endereco(endereco);
	}

    public void atualizarDados(DadosAtualizacaoPaciente dados) {
        setNome(dados.nome());
        setTelefone(dados.telefone());
        this.endereco.setLogradouro(dados.endereco().logradouro());
        this.endereco.setNumero(dados.endereco().numero());
        this.endereco.setComplemento(dados.endereco().complemento());
        this.endereco.setBairro(dados.endereco().bairro());
        this.endereco.setCidade(dados.endereco().cidade());
        this.endereco.setUf(dados.endereco().uf());
        this.endereco.setCep(dados.endereco().cep());
    }

}
