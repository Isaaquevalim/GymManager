package entities;

public class Instrutor {
    // Atributos privados que correspondem às colunas da tabela 'instrutor'
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String especialidade;
    private String horariosTrabalho;

    // Construtor completo para criação do objeto Instrutor
    public Instrutor(int id, String nome, String cpf, String telefone, String especialidade, String horariosTrabalho) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.horariosTrabalho = horariosTrabalho;
    }

    // Métodos Getters e Setters para controle de acesso aos dados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String Black) { this.especialidade = Black; }

    public String getHorariosTrabalho() { return horariosTrabalho; }
    public void setHorariosTrabalho(String horariosTrabalho) { this.horariosTrabalho = horariosTrabalho; }
}