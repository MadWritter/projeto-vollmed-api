package com.vollmed.api.model.entity;

import com.vollmed.api.model.dto.DadosAtualizacaoMedico;
import com.vollmed.api.model.dto.DadosCadastroEndereco;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.repository.MedicoRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um médico no sistema
 * @since branch medicos
 * @author Jean Maciel
 * @see MedicoRepository
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 11, unique = true)
    private String telefone;

    @Column(nullable = false, length = 6, unique = true)
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    /**
    * Construtor para essa entidade
    * @param nome até 100 caracteres
    * @param email até 100 caracteres
    * @param telefone os 11 dígitos, DDD(2) + número (9)
    * @param crm os 6 dígitos
    * @param especialidade uma das especialidades médicas
    * @param endereco os dados de endereço
    */
    public Medico(String nome, String email, String telefone, String crm, Especialidade especialidade, DadosCadastroEndereco endereco) {
        setNome(nome);
        setEmail(email);
        setTelefone(telefone);
        setCrm(crm);
        setEspecialidade(especialidade);
        setEndereco(endereco);
    }

    public Medico(DadosCadastroMedico dados) {
        this(dados.nome(), dados.email(), dados.telefone(), dados.crm(), dados.especialidade(), dados.endereco());
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

	public void setCrm(String crm) {
		if(crm != null && !crm.isBlank()) {
		  this.crm = crm;
		}
	}

	public void setEspecialidade(Especialidade especialidade) {
		if(especialidade != null) {
		  this.especialidade = especialidade;
		}
	}

	public void setEndereco(DadosCadastroEndereco endereco) {
		if(endereco != null) {
		  this.endereco = new Endereco(endereco);
		}
	}

	public void atualizarDados(DadosAtualizacaoMedico dados) {
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
