package com.vollmed.api.model.service;

import static builder.DadosCadastroPacienteBuilder.dadosDeCadastro;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.dto.DadosPacienteCadastrado;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.PacienteRepository;

import jakarta.persistence.PersistenceException;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @InjectMocks
    private PacienteService pacienteService;
    @Mock
    private PacienteRepository pacienteRepository;
    private static DadosCadastroPaciente dadosCadastroPaciente;

    @BeforeAll
    public static void setup() {
        dadosCadastroPaciente = dadosDeCadastro().validos().agora();
    }

    @Test
    public void deveCadastrarUmPaciente() {
        var pacienteCadastrado = new Paciente(dadosCadastroPaciente);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteCadastrado);

        DadosPacienteCadastrado dadosCadastrados = pacienteService.cadastrarPaciente(dadosCadastroPaciente);

        assertNotNull(dadosCadastrados);
    }

    @Test
    public void deveLancarUmaExcecao_casoOBancoEstejaForaNoCadastro() {
        when(pacienteRepository.save(any(Paciente.class))).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> pacienteService.cadastrarPaciente(dadosCadastroPaciente));
        assertEquals("Erro ao cadastrar o paciente, o banco está inoperante", ex.getMessage());
    }

    @Test
    public void deveLancarUmaExcecao_casoAlgumDadoUniqueSejaEncontrado() {
        when(pacienteRepository.save(any(Paciente.class))).thenThrow(new DataIntegrityViolationException("email nulls first"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> pacienteService.cadastrarPaciente(dadosCadastroPaciente));
        assertTrue(ex.getMessage().contains("Email já cadastrado"));
    }

    @Test
    public void deveRetornarUmPacienteAPartirDoId() {
        var pacienteCadastrado = new Paciente(dadosCadastroPaciente);
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.of(pacienteCadastrado));

        DadosPacienteCadastrado dadosCadastrados = pacienteService.findById(1L);
        assertNotNull(dadosCadastrados);
    }

    @Test
    public void deveLancarUmaExcecao_casoOBancoEstejaForaNaConsulta() {
        when(pacienteRepository.findById(anyLong())).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> pacienteService.findById(1L));
        assertEquals("Erro ao consultar o paciente, o banco está inoperante", ex.getMessage());
    }

    @Test
    public void deveRetornarUmaPaginaComDadosPacientesCadastrados() {
        var paciente1 = new Paciente(dadosCadastroPaciente);
        var paciente2 = new Paciente(dadosCadastroPaciente);
        var paciente3 = new Paciente(dadosCadastroPaciente);

        var pageImpl = new PageImpl<>(List.of(paciente1, paciente2, paciente3));

        when(pacienteRepository.findAll(any(Pageable.class))).thenReturn(pageImpl);

        Page<DadosPacienteCadastrado> paginacao = pacienteService.findAll("nome", 0);

        assertNotNull(paginacao);
        assertFalse(paginacao.isEmpty());
        assertTrue(paginacao.getSize() == 3);
    }

    @Test
    public void deveLancarUmaExcecaoNaConsulta_casoOBancoEstejaInoperante() {
        when(pacienteRepository.findAll(any(Pageable.class))).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> pacienteService.findAll("nome", 0));
        assertEquals("Erro ao consultar a lista de pacientes, o banco está inoperante", ex.getMessage());
    }
}
