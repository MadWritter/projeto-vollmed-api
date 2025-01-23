package com.vollmed.api.controller;

import static builder.DadosAtualizacaoPacienteBuilder.dadosDeAtualizacao;
import static builder.DadosCadastroPacienteBuilder.dadosDeCadastro;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
import com.vollmed.api.model.dto.DadosAtualizacaoPaciente;
import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.dto.DadosPacienteCadastrado;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.service.PacienteService;

@WebMvcTest
public class PacienteControllerTest {

    @MockitoBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static DadosCadastroPaciente dadosCadastroPaciente;
    private static DadosAtualizacaoPaciente dadosAtualizacaoPaciente;

    @BeforeAll
    public static void setup() {
        dadosCadastroPaciente = dadosDeCadastro().validos().agora();
        dadosAtualizacaoPaciente = dadosDeAtualizacao().validos().agora();
    }

    @Test
    public void deveCadastrarUmPaciente() throws Exception {
        var pacienteCadastrado = new Paciente(dadosCadastroPaciente);
        when(pacienteService.cadastrarPaciente(any(DadosCadastroPaciente.class)))
            .thenReturn(new DadosPacienteCadastrado(pacienteCadastrado));

        mockMvc.perform(post("/paciente")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(dadosCadastroPaciente)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value(dadosCadastroPaciente.nome()));
    }

    @Test
    public void deveRetornarUmPacienteCadastrado() throws Exception {
        var pacienteCadastrado = new Paciente(dadosCadastroPaciente);
        var dadosPacienteCadastrado = new DadosPacienteCadastrado(pacienteCadastrado);

        when(pacienteService.findById(anyLong())).thenReturn(dadosPacienteCadastrado);

        mockMvc.perform(get("/paciente/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.CPF").value(dadosCadastroPaciente.cpf()));
    }

    @Test
    public void deveRetornar404_casoPacienteNaoExista() throws Exception {
        when(pacienteService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/paciente/2"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deveRetornarUmaPaginaComOsPacientesCadastrados() throws Exception {
        var paciente1 = new Paciente(dadosCadastroPaciente);
        var paciente2 = new Paciente(dadosCadastroPaciente);
        var paciente3 = new Paciente(dadosCadastroPaciente);
        var paciente4 = new Paciente(dadosCadastroPaciente);

        var listPacientes = List.of(paciente1, paciente2, paciente3, paciente4);

        when(pacienteService.findAll(anyString(), anyInt()))
            .thenReturn(new PageImpl<>(listPacientes.stream().map(DadosPacienteCadastrado::new).toList()));

        mockMvc.perform(get("/paciente")
            .param("sort", "nome")
            .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    public void deveAtualizarUmPacienteCadastrado() throws Exception {
        var pacienteCadastrado = new Paciente(dadosCadastroPaciente);
        pacienteCadastrado.atualizarDados(dadosAtualizacaoPaciente);
        var dadosPacienteAtualizado = new DadosPacienteCadastrado(pacienteCadastrado);

        when(pacienteService.atualizarPaciente(anyLong(), any(DadosAtualizacaoPaciente.class))).thenReturn(dadosPacienteAtualizado);

        mockMvc.perform(put("/paciente/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dadosAtualizacaoPaciente)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value(dadosAtualizacaoPaciente.nome()));
    }
}
