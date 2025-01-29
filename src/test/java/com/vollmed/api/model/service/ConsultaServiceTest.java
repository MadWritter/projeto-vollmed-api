package com.vollmed.api.model.service;

import static builder.DadosCadastroConsultaBuilder.dadosParaCadastrarConsulta;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.dto.DadosConsultaCadastrada;
import com.vollmed.api.model.entity.Consulta;
import com.vollmed.api.model.entity.Especialidade;
import com.vollmed.api.model.entity.Medico;
import com.vollmed.api.model.entity.Paciente;
import com.vollmed.api.model.repository.ConsultaRepository;
import com.vollmed.api.model.repository.MedicoRepository;
import com.vollmed.api.model.repository.PacienteRepository;

import builder.DadosCadastroMedicoBuilder;
import builder.DadosCadastroPacienteBuilder;

@ExtendWith(MockitoExtension.class)
public class ConsultaServiceTest {

    @InjectMocks
    private ConsultaService consultaService;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    private static DadosCadastroConsulta dadosDeCadastro;

    @BeforeAll
    public static void setup() {
        dadosDeCadastro = dadosParaCadastrarConsulta().validos().agora();
    }

    @Test
    public void deveCadastrarUmaConsultaMedica() {
        var pacienteCadastrado = new Paciente(DadosCadastroPacienteBuilder.dadosDeCadastro().validos().agora());
        var medicoCadastrado = new Medico(DadosCadastroMedicoBuilder.dadosDeCadastro().validos().agora());
        var consultaCadastrada = new Consulta(pacienteCadastrado, medicoCadastrado, dadosDeCadastro.dataDaConsulta());

        when(pacienteRepository.findByIdAndAtivoTrue(anyLong())).thenReturn(Optional.of(pacienteCadastrado));
        when(medicoRepository.findAllByEspecialidadeAndAtivoTrue(any(Especialidade.class))).thenReturn(List.of(medicoCadastrado));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaCadastrada);

        DadosConsultaCadastrada dadosConsultaCadastrada = consultaService.cadastrarConsulta(dadosDeCadastro);
        assertNotNull(dadosConsultaCadastrada);
    }

    //TODO implementar as validações do card no trello
}
