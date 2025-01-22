package com.vollmed.api.controller;

import static builder.DadosCadastroPacienteBuilder.dadosDeCadastro;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @BeforeAll
    public static void setup() {
        dadosCadastroPaciente = dadosDeCadastro().validos().agora();
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
}
