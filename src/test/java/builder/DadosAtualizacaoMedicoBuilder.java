package builder;

import com.vollmed.api.model.dto.DadosAtualizacaoEndereco;
import com.vollmed.api.model.dto.DadosAtualizacaoMedico;

public class DadosAtualizacaoMedicoBuilder {
    private DadosAtualizacaoMedico dados;

    private DadosAtualizacaoMedicoBuilder() {}

    public static DadosAtualizacaoMedicoBuilder dadosDeAtualizacao() {
        return new DadosAtualizacaoMedicoBuilder();
    }

    public DadosAtualizacaoMedicoBuilder validos() {
        var dadosAtualizacaoEndereco = new DadosAtualizacaoEndereco("Rua das Nuvens", null, null, null, null, null, null);
        this.dados = new DadosAtualizacaoMedico("Teste Atualizacao", null, dadosAtualizacaoEndereco);
        return this;
    }

    public DadosAtualizacaoMedico agora() {
        return this.dados;
    }
}
