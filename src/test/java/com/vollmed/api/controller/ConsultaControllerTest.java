package com.vollmed.api.controller;

import static builder.DadosCadastroConsultaBuilder.dadosParaCadastrarConsulta;
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
import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.service.ConsultaService;

import builder.DadosCadastroMedicoBuilder;
import builder.DadosCadastroPacienteBuilder;

@WebMvcTest(ConsultaController.class)
public class ConsultaControllerTest {

    @MockitoBean
    private ConsultaService consultaService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private static DadosCadastroConsulta dadosDeCadastro;

    @BeforeAll
    public static void setup() {
        dadosDeCadastro = dadosParaCadastrarConsulta().validos().agora();
    }

    @Test
    public void deveCadastrarUmaConsulta() throws Exception {
        var pacienteCadastrado = new Paciente(DadosCadastroPacienteBuilder.dadosDeCadastro().validos().agora());
        var medicoCadastrado = new Medico(DadosCadastroMedicoBuilder.dadosDeCadastro().validos().agora());
        var consultaCadastrada = new Consulta(pacienteCadastrado, medicoCadastrado, dadosDeCadastro.dataDaConsulta());
        var dto = new DadosConsultaCadastrada(consultaCadastrada);
        when(consultaService.cadastrarConsulta(any(DadosCadastroConsulta.class))).thenReturn(dto);

        mockMvc.perform(post("/consulta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dadosDeCadastro)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome_paciente").value(pacienteCadastrado.getNome()));
    }
}
