package entities;

import java.time.LocalDateTime;

public class Aula {
    // Atributos privados mapeados a partir da tabela 'aula'
    private int id;
    private String nome;
    private String descricao;
    private int capacidadeMaxima;
    private LocalDateTime horario; // LocalDateTime lida com data e hora da agenda da aula
    private int duracaoMinutos;
    private Instrutor instrutor; // Relacionamento 1:N (Chave estrangeira id_instrutor)

    // Construtor completo da classe Aula
    public Aula(int id, String nome, String descricao, int capacidademaxima, LocalDateTime horario, int duracaoMinutos, Instrutor instrutor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.capacidadeMaxima = capacidademaxima;
        this.horario = horario;
        this.duracaoMinutos = duracaoMinutos;
        this.instrutor = instrutor;
    }

    // Métodos Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public LocalDateTime getHorario() { return horario; }
    public void setHorario(LocalDateTime horario) { this.horario = horario; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public Instrutor getInstrutor() { return instrutor; }
    public void setInstrutor(Instrutor instrutor) { this.instrutor = instrutor; }
}