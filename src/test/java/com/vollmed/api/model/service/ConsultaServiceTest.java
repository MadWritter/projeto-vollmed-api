package com.vollmed.api.model.service;

import static builder.DadosCadastroConsultaBuilder.dadosParaCadastrarConsulta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vollmed.api.model.component.ClockConfig;
import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Especialidade;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.ConsultaRepository;
import com.vollmed.api.model.repository.MedicoRepository;
import com.vollmed.api.model.repository.PacienteRepository;

import builder.DadosCadastroMedicoBuilder;
import builder.DadosCadastroPacienteBuilder;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ConsultaServiceTest {

    @InjectMocks
    private ConsultaService consultaService;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Spy
    private ClockConfig clockConfig;

    private static DadosCadastroConsulta dadosDeCadastro;

    @BeforeEach
    public void setup() {
        dadosDeCadastro = dadosParaCadastrarConsulta().validos().agora();
        LocalDateTime dataDoTeste = LocalDateTime.of(LocalDate.of(2025, 1, 28), LocalTime.of(8, 10));
        Clock clock = Clock.fixed(dataDoTeste.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        clockConfig.setClock(clock);
    }

    @Test
    public void deveCadastrarUmaConsultaMedica() {
        var pacienteCadastrado = new Paciente(DadosCadastroPacienteBuilder.dadosDeCadastro().validos().agora());
        var medicoCadastrado = new Medico(DadosCadastroMedicoBuilder.dadosDeCadastro().validos().agora());
        var consultaCadastrada = new Consulta(pacienteCadastrado, medicoCadastrado, dadosDeCadastro.dataDaConsulta());

        when(pacienteRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.of(pacienteCadastrado));
        when(medicoRepository.findAllByEspecialidadeAndAtivoTrue(any(Especialidade.class))).thenReturn(List.of(medicoCadastrado));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaCadastrada);

        DadosConsultaCadastrada dadosConsultaCadastrada = consultaService.cadastrarConsulta(dadosDeCadastro);
        assertNotNull(dadosConsultaCadastrada);
    }

    @Test
    public void deveLancarExcecao_casoDataAgendamentoSejaAntesDaDataAtual() {
        // Data antes da data atual
        LocalDateTime dataConsulta = LocalDateTime.of(LocalDate.of(2025, 1, 27), LocalTime.of(8, 12));
        var consultaComDataAnterior = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, dataConsulta);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> consultaService.cadastrarConsulta(consultaComDataAnterior));

        assertEquals("A data informada é anterior a data atual", ex.getMessage());
    }

    @Test
    public void deveLancarExcecao_casoAgendeForaDeHora() {
        // a clínica funciona entre 7 e 19h

        LocalDateTime dataAntesDeSete = LocalDateTime.of(LocalDate.of(2025, 1, 29), LocalTime.of(6, 27));
        LocalDateTime dataDepoisDeDezenove = LocalDateTime.of(LocalDate.of(2025, 1, 29), LocalTime.of(21, 43));

        var consultaAntesDeSete = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, dataAntesDeSete);
        var consultaDepoisDeDezenove = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, dataDepoisDeDezenove);

        IllegalArgumentException exAntesDeSete = assertThrows(IllegalArgumentException.class, () -> consultaService.cadastrarConsulta(consultaAntesDeSete));
        IllegalArgumentException exDepoisDeDezenove = assertThrows(IllegalArgumentException.class, () -> consultaService.cadastrarConsulta(consultaDepoisDeDezenove));

        assertEquals("As consultas devem ser agendadas entre 7h e 19h", exAntesDeSete.getMessage());
        assertEquals("As consultas devem ser agendadas entre 7h e 19h", exDepoisDeDezenove.getMessage());
    }

    @Test
    public void deveLancarExcecao_casoPacienteInativo() {
        LocalDateTime dataDaConsulta = LocalDateTime.of(LocalDate.of(2025, 1, 29), LocalTime.of(8, 32));
        var dadosConsulta = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, dataDaConsulta);
        when(pacienteRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> consultaService.cadastrarConsulta(dadosConsulta));
        assertEquals("Paciente informado não foi encontrado", ex.getMessage());
    }

    @Test
    public void deveLancarExcecao_casoMedicoInativo() {
        var pacienteCadastrado = new Paciente(DadosCadastroPacienteBuilder.dadosDeCadastro().validos().agora());
        LocalDateTime dataDaConsulta = LocalDateTime.of(LocalDate.of(2025, 1, 29), LocalTime.of(8, 32));
        var dadosConsulta = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, dataDaConsulta);

        when(pacienteRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.of(pacienteCadastrado));
        when(medicoRepository.findAllByEspecialidadeAndAtivoTrue(any(Especialidade.class))).thenReturn(List.of());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> consultaService.cadastrarConsulta(dadosConsulta));
        assertEquals("Nenhum médico com a especialidade informada disponível", ex.getMessage());
    }

    @Test
    public void deveLancarExcecao_casoPacienteJaTenhaConsultaNoDia() {
        var pacienteCadastrado = new Paciente(DadosCadastroPacienteBuilder.dadosDeCadastro().validos().agora());
        var medicoCadastrado = new Medico(DadosCadastroMedicoBuilder.dadosDeCadastro().validos().agora());

        // simulando a data da consulta existente neste mesmo dia do agendamento
        var consultaCadastrada = new Consulta(pacienteCadastrado, medicoCadastrado, dadosDeCadastro.dataDaConsulta());

        // a data da consulta informada na requisição
        LocalDateTime dataDaConsulta = LocalDateTime.of(LocalDate.of(2025, 1, 29), LocalTime.of(8, 45));
        var dadosConsulta = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, dataDaConsulta);

        when(pacienteRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.of(pacienteCadastrado));
        when(consultaRepository.findByDataAndPacienteAndAgendada(any(LocalDateTime.class), any(Paciente.class))).thenReturn(Optional.of(consultaCadastrada));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> consultaService.cadastrarConsulta(dadosConsulta));
        assertEquals("O paciente informado já tem uma consulta cadastrada nesta data", ex.getMessage());
    }
}
