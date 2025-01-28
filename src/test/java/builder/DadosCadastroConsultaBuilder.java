package builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.vollmed.api.model.dto.DadosCadastroConsulta;
import com.vollmed.api.model.entity.Especialidade;

public class DadosCadastroConsultaBuilder {
    
    private DadosCadastroConsulta dados;

    private DadosCadastroConsultaBuilder() {}

    public static DadosCadastroConsultaBuilder dadosDeCadastro() {
        return new DadosCadastroConsultaBuilder();
    }

    public DadosCadastroConsultaBuilder validos() {
        LocalDate data = LocalDate.of(2025, 1, 28);
        LocalTime hora = LocalTime.of(9, 35);
        this.dados = new DadosCadastroConsulta(1L, Especialidade.CARDIOLOGIA, LocalDateTime.of(data, hora));
        return this;
    }

    public DadosCadastroConsulta agora() {
        return this.dados;
    }
}
