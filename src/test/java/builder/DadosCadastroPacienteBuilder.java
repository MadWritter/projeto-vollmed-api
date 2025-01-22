package builder;

import com.vollmed.api.model.dto.DadosCadastroEndereco;
import com.vollmed.api.model.dto.DadosCadastroPaciente;
import com.vollmed.api.model.entity.UF;

public class DadosCadastroPacienteBuilder {

    private DadosCadastroPaciente dados;

    private DadosCadastroPacienteBuilder() {}

    public static DadosCadastroPacienteBuilder dadosDeCadastro() {
        return new DadosCadastroPacienteBuilder();
    }

    public DadosCadastroPacienteBuilder validos() {
        var endereco = new DadosCadastroEndereco("teste", null, null, "teste", "teste", UF.AM, "66789000");
        this.dados = new DadosCadastroPaciente("Teste", "teste@gmail.com", "78988887654", "12345678900", endereco);
        return this;
    }

    public DadosCadastroPaciente agora() {
        return this.dados;
    }
}
