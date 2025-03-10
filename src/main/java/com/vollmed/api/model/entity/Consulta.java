package com.vollmed.api.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma consulta médica
 *
 * @since branch consultas
 * @author Jean Maciel
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false, name = "data_da_consulta")
    private LocalDateTime dataDaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento", length = 20)
    private MotivoCancelamento motivoCancelamento;

    @Column(name = "observacao_cancelamento", length = 100)
    private String observacaoCancelamento;

    /**
     * Construtor para uma nova consulta
     *
     * @param paciente o paciente que irá se consultar
     * @param medico o médico com a especialidade solicitada pelo paciente
     * @param dataDaConsulta a data que a consulta será efetuada
     * @apiNote O Status inicial de uma consulta com esse construtor sempre
     * será AGENDADA.
     */
    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataDaConsulta) {
        setPaciente(paciente);
        setMedico(medico);
        setDataDaConsulta(dataDaConsulta);
        setStatus(Status.AGENDADA);
    }

    public void setPaciente(Paciente paciente) {
        if (paciente != null) {
            this.paciente = paciente;
        }
    }

    public void setMedico(Medico medico) {
        if (medico != null) {
            this.medico = medico;
        }
    }

    public void setDataDaConsulta(LocalDateTime dataDaConsulta) {
        if (dataDaConsulta != null) {
            this.dataDaConsulta = dataDaConsulta;
        }
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void setMotivoCancelamento(MotivoCancelamento motivoCancelamento) {
        if(motivoCancelamento != null) {
            this.motivoCancelamento = motivoCancelamento;
        }
    }

    public void setObservacaoCancelamento(String observacao) {
        if(observacao != null) {
            this.observacaoCancelamento = observacao;
        }
    }
}
