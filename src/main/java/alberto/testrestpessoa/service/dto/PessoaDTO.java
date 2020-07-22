package alberto.testrestpessoa.service.dto;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

public class PessoaDTO implements Serializable {

    private Integer id;

    @NotNull
    private String nome;

    @NotNull
    private String cpf;

    @NotNull
    private Instant dataNascimento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Instant getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PessoaDTO)) return false;
        PessoaDTO pessoaDTO = (PessoaDTO) o;
        return Objects.equal(id, pessoaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PessoaDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
