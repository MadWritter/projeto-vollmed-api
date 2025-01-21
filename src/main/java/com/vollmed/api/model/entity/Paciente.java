package com.vollmed.api.model.entity;

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
    public Paciente(String nome, String email, String telefone, String cpf, Endereco endereco) {
        setNome(nome);
        setEmail(email);
        setTelefone(telefone);
        setCpf(cpf);
        setEndereco(endereco);
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
}
