package entities;

import java.time.LocalDate;

// Objetivo: Representar o cliente da academia.
// Regra de Banco: O CPF mapeia a chave primária (PK) 'cpf_aluno' do tipo VARCHAR.
public class Aluno {

    // Atributos privados garantindo o pilar do encapsulamento em POO.
    private String cpfAluno;
    private String nomeCompleto;
    private LocalDate dataNascimento;

    // Construtor completo para inicializar o objeto com os dados vindos do banco de dados.
    public Aluno(String cpfAluno, String nomeCompleto, LocalDate dataNascimento) {
        this.cpfAluno = cpfAluno;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
    }

    // Métodos Getters (leitura) e Setters (escrita) para acesso seguro aos atributos.
    public String getCpfAluno() {
        return cpfAluno;
    }

    public void setCpfAluno(String cpfAluno) {
        this.cpfAluno = cpfAluno;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}