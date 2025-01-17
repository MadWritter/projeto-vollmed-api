package com.vollmed.api.model.service;

import static builder.DadosCadastroMedicoBuilder.dadosDeCadastro;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.repository.MedicoRepository;

import jakarta.persistence.PersistenceException;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @InjectMocks
    private MedicoService medicoService;
    @Mock
    private MedicoRepository medicoRepository;
    private static DadosCadastroMedico dadosCadastroMedico;

    @BeforeAll
    public static void setup() {
        dadosCadastroMedico = dadosDeCadastro().validos().agora();
    }

    @Test
    public void deveCadastrarUmMedico() {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoCadastrado);

        DadosMedicoCadastrado dadosMedicoCadastrado = medicoService.cadastrarMedico(dadosCadastroMedico);
        assertNotNull(dadosMedicoCadastrado);
    }

    @Test
    public void deveLancarUmaExcecao_casoOBancoEstejaFora() {
        when(medicoRepository.save(any(Medico.class))).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> medicoService.cadastrarMedico(dadosCadastroMedico));
        assertEquals("Erro ao cadastrar o médico, o banco está inoperante", ex.getMessage());
    }

    @Test
    public void deveLancarUmaExcecao_casoAlgumDadoUniqueSejaEncontrado() {
        when(medicoRepository.save(any(Medico.class))).thenThrow(new DataIntegrityViolationException("email nulls first"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> medicoService.cadastrarMedico(dadosCadastroMedico));
        assertTrue(ex.getMessage().contains("Email já cadastrado"));
    }

    @Test
    public void deveRetornarUmMedicoCadastrado() {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        when(medicoRepository.findById(anyLong())).thenReturn(Optional.of(medicoCadastrado));

        DadosMedicoCadastrado dadosMedicoCadastrado = medicoService.findById(1L);
        assertNotNull(dadosMedicoCadastrado);
    }

    @Test
    public void deveLancarUmaExcecaoAoConsultar_casoBancoFora() {
        when(medicoRepository.findById(anyLong())).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> medicoService.findById(1L));
        assertEquals("Erro ao consultar um médico cadastrado, o banco está inoperante", ex.getMessage());
    }
}
