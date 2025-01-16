package com.vollmed.api.controller;

import static builder.DadosCadastroMedicoBuilder.dadosDeCadastro;
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

    @BeforeAll
    public static void setup() {
        dadosCadastroMedico = dadosDeCadastro().validos().agora();
    }

    @Test
    public void deveCadastrarUmMedico() throws Exception {
        var medicoCadastrado = new Medico(dadosCadastroMedico);
        when(medicoService.cadastrarMedico(any(DadosCadastroMedico.class))).thenReturn(new DadosMedicoCadastrado(medicoCadastrado));

        mockMvc.perform(post("/medico")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dadosCadastroMedico)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value(dadosCadastroMedico.nome()));
    }
}
