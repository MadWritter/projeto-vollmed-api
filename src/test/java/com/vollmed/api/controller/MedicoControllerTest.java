package com.vollmed.api.controller;

import static builder.DadosAtualizacaoMedicoBuilder.dadosDeAtualizacao;
import static builder.DadosCadastroMedicoBuilder.dadosDeCadastro;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vollmed.api.model.dto.DadosAtualizacaoMedico;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.dto.DadosMedicoCadastrado;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.service.MedicoService;

@WebMvcTest
public class MedicoControllerTest {

    @MockitoBean
    private MedicoService medicoService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static DadosCadastroMedico dadosCadastroMedico;
    private static DadosAtualizacaoMedico dadosAtualizacaoMedico;

    @BeforeAll
    public static void setup() {
        dadosCadastroMedico = dadosDeCadastro().validos().agora();
        dadosAtualizacaoMedico = dadosDeAtualizacao().validos().agora();
    }

    @Test
    public void deveCadastrarUmMedico() throws Exception {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        when(medicoService.cadastrarMedico(any(DadosCadastroMedico.class)))
            .thenReturn(new DadosMedicoCadastrado(medicoCadastrado));

        mockMvc.perform(post("/medico")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dadosCadastroMedico)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value(dadosCadastroMedico.nome()));
    }

    @Test
    public void deveRetornarUmMedicoCadastrado() throws Exception {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        var dadosMedicoCadastrado = new DadosMedicoCadastrado(medicoCadastrado);

        when(medicoService.findById(anyLong())).thenReturn(dadosMedicoCadastrado);

        mockMvc.perform(get("/medico/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.CRM").value(dadosMedicoCadastrado.crm()));
    }

    @Test
    public void deveRetornar404CasoMedicoNaoExista() throws Exception {
        when(medicoService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/medico/2"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarUmaPaginaComOsMedicosCadastrados() throws Exception {
        var medico1 = new Medico(dadosCadastroMedico);
        var medico2 = new Medico(dadosCadastroMedico);
        var medico3 = new Medico(dadosCadastroMedico);
        var medico4 = new Medico(dadosCadastroMedico);
        var listMedicos = List.of(medico1, medico2, medico3, medico4);

        when(medicoService.findAll(anyString(), anyInt()))
            .thenReturn(new PageImpl<>(listMedicos.stream().map(DadosMedicoCadastrado::new).toList()));

        mockMvc.perform(get("/medico")
            .param("sort", "nome")
            .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    public void deveRetornarUmDTOAtualizado() throws Exception {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        medicoCadastrado.atualizarDados(dadosAtualizacaoMedico);
        var dadosMedicoAtualizado = new DadosMedicoCadastrado(medicoCadastrado);

        when(medicoService.atualizarDados(anyLong(), any(DadosAtualizacaoMedico.class))).thenReturn(dadosMedicoAtualizado);

        mockMvc.perform(put("/medico/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dadosAtualizacaoMedico)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value(dadosAtualizacaoMedico.nome()));
    }

    @Test
    public void deveExcluirUmMedico() throws Exception {
        when(medicoService.excluirMedico(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/medico/1"))
            .andExpect(status().isNoContent());
    }
}
