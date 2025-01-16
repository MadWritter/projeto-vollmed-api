package com.vollmed.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.vollmed.api.model.service.MedicoService;

@WebMvcTest
public class MedicoControllerTest {

    @MockitoBean
    private MedicoService medicoService;
    @Autowired
    private MockMvc mockMvc;

    //TODO implementar o controller
}
