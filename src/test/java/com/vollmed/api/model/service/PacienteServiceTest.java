package com.vollmed.api.model.service;

import static builder.DadosCadastroPacienteBuilder.dadosDeCadastro;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

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
}
