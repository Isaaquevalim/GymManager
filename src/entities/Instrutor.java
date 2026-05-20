package entities;

public class Instrutor {
    private int idInstrutor; // -> Atualizado para o padrão do Dicionário de Dados
    private String nome;
    private String cpf;
    private String telefone;
    private String especialidade;
    private String horariosTrabalho;

    public Instrutor(int idInstrutor, String nome, String cpf, String telefone, String especialidade, String horariosTrabalho) {
        this.idInstrutor = idInstrutor;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.horariosTrabalho = horariosTrabalho;
    }

    public int getIdInstrutor() { return idInstrutor; }
    public void setIdInstrutor(int idInstrutor) { this.idInstrutor = idInstrutor; }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEspecialidade() { return especialidade; }
    public String getHorariosTrabalho() { return horariosTrabalho; }
}