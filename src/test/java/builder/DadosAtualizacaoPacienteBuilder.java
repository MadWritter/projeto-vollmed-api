package builder;

import com.vollmed.api.model.dto.DadosAtualizacaoEndereco;
import com.vollmed.api.model.dto.DadosAtualizacaoPaciente;
import com.vollmed.api.model.entity.UF;

public class DadosAtualizacaoPacienteBuilder {

    private DadosAtualizacaoPaciente dados;

    private DadosAtualizacaoPacienteBuilder() {}

    public static DadosAtualizacaoPacienteBuilder dadosDeAtualizacao() {
        return new DadosAtualizacaoPacienteBuilder();
    }

    public DadosAtualizacaoPacienteBuilder validos() {
        var dadosAtualizacaoEndereco = new DadosAtualizacaoEndereco("teste logradouro", 1, "teste", "teste", "teste", UF.CE, "77899666");
        this.dados = new DadosAtualizacaoPaciente("Teste2", "91978789090", dadosAtualizacaoEndereco);
        return this;
    }

    public DadosAtualizacaoPaciente agora() {
        return this.dados;
    }
}
