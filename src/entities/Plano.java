package entities;

public class Plano {
    // Atributos privados que representam as colunas da tabela 'plano'
    private int id;
    private String nome;
    private String descricao;
    private double valorMensal;
    private int duracaoMeses;
    private String beneficios;

    // Construtor completo para instanciar o plano com todos os dados vindos do banco
    public Plano(int id, String nome, String descricao, double valorMensal, int duracaoMeses, String beneficios) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valorMensal = valorMensal;
        this.duracaoMeses = duracaoMeses;
        this.beneficios = beneficios;
    }

    // Métodos Getters e Setters para leitura e modificação segura dos atributos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorMensal() { return valorMensal; }
    public void setValorMensal(double valorMensal) { this.valorMensal = valorMensal; }

    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
}