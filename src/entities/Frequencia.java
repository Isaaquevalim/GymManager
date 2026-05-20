package entities;

import java.time.LocalDateTime;

public class Frequencia {
    // Atributos privados mapeados a partir da tabela 'frequencia'
    private int id;
    private Aluno aluno; // Relacionamento 1:N (Chave estrangeira id_aluno)
    private LocalDateTime dataHoraEntrada; // Registra momento exato da passagem pela catraca

    // Construtor completo da classe Frequencia
    public Frequencia(int id, Aluno aluno, LocalDateTime dataHoraEntrada) {
        this.id = id;
        this.aluno = aluno;
        this.dataHoraEntrada = dataHoraEntrada;
    }

    // Métodos Getters e Setters de encapsulamento
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public LocalDateTime getDataHoraEntrada() { return dataHoraEntrada; }
    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) { this.dataHoraEntrada = dataHoraEntrada; }
}