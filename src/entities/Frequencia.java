package entities;

// Objetivo: Atuar como a entidade que resolve o relacionamento Muitos-para-Muitos (N:M) entre Alunos e Aulas.
public class Frequencia {

    private int idFrequencia;
    private Aluno aluno; // Associação com o Aluno que realizou o check-in.
    private Aula aula; // Associação com a Aula assistida.
    private int quantidade;

    public Frequencia(int idFrequencia, Aluno aluno, Aula aula, int quantidade) {
        this.idFrequencia = idFrequencia;
        this.aluno = aluno;
        this.aula = aula;
        this.quantidade = quantidade;
    }

    public int getIdFrequencia() {
        return idFrequencia;
    }

    public void setIdFrequencia(int idFrequencia) {
        this.idFrequencia = idFrequencia;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}