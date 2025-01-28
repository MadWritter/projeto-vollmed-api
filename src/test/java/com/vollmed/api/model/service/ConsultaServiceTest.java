package com.vollmed.api.model.service;

import static builder.DadosCadastroConsultaBuilder.dadosDeCadastro;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.repository.ConsultaRepository;

@ExtendWith(MockitoExtension.class)
public class ConsultaServiceTest {

    @InjectMocks
    private ConsultaService consultaService;

    @Mock
    private ConsultaRepository consultaRepository;

    private static DadosCadastroConsulta dadosDeCadastro;

    @BeforeAll
    public static void setup() {
        dadosDeCadastro = dadosDeCadastro().validos().agora();
    }

    //TODO implementar os testes do serviço
}
