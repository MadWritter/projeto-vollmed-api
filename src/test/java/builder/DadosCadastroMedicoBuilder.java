package builder;

import com.vollmed.api.model.dto.DadosCadastroEndereco;
import com.vollmed.api.model.dto.DadosCadastroMedico;
import com.vollmed.api.model.entity.Especialidade;
import com.vollmed.api.model.entity.UF;

public class DadosCadastroMedicoBuilder {
    private DadosCadastroMedico dados;

    private DadosCadastroMedicoBuilder() {}

    public static DadosCadastroMedicoBuilder dadosDeCadastro() {
        return new DadosCadastroMedicoBuilder();
    }

    public DadosCadastroMedicoBuilder validos() {
        var endereco = new DadosCadastroEndereco("teste", null, null, "teste_bairro", "teste_cidade", UF.SP, "66789000");
        this.dados = new DadosCadastroMedico("Teste", "teste@gmail.com", "88999990000", "777888", Especialidade.CARDIOLOGIA, endereco);
        return this;
    }

    public DadosCadastroMedico agora() {
        return this.dados;
    }
}
