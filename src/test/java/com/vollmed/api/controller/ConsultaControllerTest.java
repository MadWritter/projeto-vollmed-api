package com.vollmed.api.controller;

import static builder.DadosCadastroConsultaBuilder.dadosDeCadastro;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vollmed.api.model.dto.DadosCadastroConsulta;

@WebMvcTest(ConsultaController.class)
public class ConsultaControllerTest {

    @MockitoBean
    private ConsultaController consultaController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static DadosCadastroConsulta dadosDeCadastro;

    @BeforeAll
    public static void setup() {
        dadosDeCadastro = dadosDeCadastro().validos().agora();
    }

    //TODO implementar os testes do controller
}
