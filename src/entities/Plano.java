package entities;

public class Plano {

    private int idPlano;
    private String nome;
    private String descricao;
    private double valorMensal;
    private int duracaoMeses;
    private String beneficios;

    public Plano(int idPlano, String nome, String descricao, double valorMensal, int duracaoMeses, String beneficios) {
        this.idPlano = idPlano;
        this.nome = nome;
        this.descricao = descricao;
        this.valorMensal = valorMensal;
        this.duracaoMeses = duracaoMeses;
        this.beneficios = beneficios;
    }

    public int getIdPlano() { return idPlano; }
    public void setIdPlano(int idPlano) { this.idPlano = idPlano; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    // Estes foram os métodos que faltaram e que estavam a quebrar o DAO!
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorMensal() { return valorMensal; }
    public void setValorMensal(double valorMensal) { this.valorMensal = valorMensal; }

    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
}