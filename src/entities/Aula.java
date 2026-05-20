package entities;

import java.time.LocalDateTime;

public class Aula {
    private int idAula; // -> Atualizado para o padrão do Dicionário de Dados
    private String nome;
    private String descricao;
    private int capacidadeMaxima;
    private LocalDateTime horario;
    private int duracaoMinutos;
    private Instrutor instrutor;

    public Aula(int idAula, String nome, String descricao, int capacidadeMaxima, LocalDateTime horario, int duracaoMinutos, Instrutor instrutor) {
        this.idAula = idAula;
        this.nome = nome;
        this.descricao = descricao;
        this.capacidadeMaxima = capacidadeMaxima;
        this.horario = horario;
        this.duracaoMinutos = duracaoMinutos;
        this.instrutor = instrutor;
    }

    public int getIdAula() { return idAula; }
    public void setIdAula(int idAula) { this.idAula = idAula; }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public LocalDateTime getHorario() { return horario; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    public Instrutor getInstrutor() { return instrutor; }
}