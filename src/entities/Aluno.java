package entities;

import java.time.LocalDate;

public class Aluno {
    // Atributo renomeado para corresponder a 'id_aluno'
    private int idAluno;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private LocalDate dataMatricula;
    private Plano planoAtivo;


    public Aluno(int idAluno, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, LocalDate dataMatricula, Plano planoAtivo) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.dataMatricula = dataMatricula;
        this.planoAtivo = planoAtivo;
    }

    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public LocalDate getDataMatricula() { return dataMatricula; }
    public Plano getPlanoAtivo() { return planoAtivo; }

    public LocalDate calcularVencimentoPlano() {
        if (this.planoAtivo != null) {
            return this.dataMatricula.plusMonths(this.planoAtivo.getDuracaoMeses());
        }
        return null;
    }

    public boolean isPlanoAtivo() {
        LocalDate vencimento = calcularVencimentoPlano();
        return vencimento != null && LocalDate.now().isBefore(vencimento);
    }
}