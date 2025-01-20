package com.vollmed.api.model.service;

import static builder.DadosAtualizacaoMedicoBuilder.dadosDeAtualizacao;
import static builder.DadosCadastroMedicoBuilder.dadosDeCadastro;
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

import com.vollmed.api.model.dto.DadosAtualizacaoMedico;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.repository.MedicoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @InjectMocks
    private MedicoService medicoService;
    @Mock
    private MedicoRepository medicoRepository;
    private static DadosCadastroMedico dadosCadastroMedico;
    private static DadosAtualizacaoMedico dadosDeAtualizacao;

    @BeforeAll
    public static void setup() {
        dadosCadastroMedico = dadosDeCadastro().validos().agora();
        dadosDeAtualizacao = dadosDeAtualizacao().validos().agora();
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
        when(medicoRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.of(medicoCadastrado));

        DadosMedicoCadastrado dadosMedicoCadastrado = medicoService.findById(1L);
        assertNotNull(dadosMedicoCadastrado);
    }

    @Test
    public void deveLancarUmaExcecaoAoConsultar_casoBancoFora() {
        when(medicoRepository.findByIdAndAtivoTrue(anyLong())).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> medicoService.findById(1L));
        assertEquals("Erro ao consultar um médico cadastrado, o banco está inoperante", ex.getMessage());
    }

    @Test
    public void deveRetornarUmaPaginaComOsDadosMedicosCadastrados() {
        var medico1 = new Medico(dadosCadastroMedico);
        var medico2 = new Medico(dadosCadastroMedico);
        var medico3 = new Medico(dadosCadastroMedico);

        var pageImpl = new PageImpl<>(List.of(medico1, medico2, medico3));

        when(medicoRepository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(pageImpl);

        Page<DadosMedicoCadastrado> page = medicoService.findAll("nome", 1);
        assertNotNull(page);
        assertFalse(page.isEmpty());
    }

    @Test
    public void deveLancarUmaExcecaoAoConsultarALista_casoBancoFora() {
        when(medicoRepository.findAllByAtivoTrue(any(Pageable.class))).thenThrow(PersistenceException.class);

        PersistenceException ex = assertThrows(PersistenceException.class, () -> medicoService.findAll("nome", 1));
        assertEquals("Erro ao consultar a lista de médicos, o banco está inoperante", ex.getMessage());
    }

    @Test
    public void deveAtualizarDadosMedicoCadastrado() {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        when(medicoRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.of(medicoCadastrado));

        DadosMedicoCadastrado dadosAtualizados = medicoService.atualizarDados(1L, dadosDeAtualizacao);
        assertNotNull(dadosAtualizados);
    }

    @Test
    public void deveLancarExcecao_casoIdNaoTenhaCorrespondente() {
        when(medicoRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> medicoService.atualizarDados(1L, dadosDeAtualizacao));
        assertEquals("O ID informado não tem um correspondente", ex.getMessage());
    }
}
