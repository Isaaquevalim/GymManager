package entities;

import java.time.LocalDate;

public class Aluno {
    // Atributos privados mapeados a partir da tabela 'aluno'
    private int id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento; // Utiliza o tipo LocalDate para gerenciar datas sem hora
    private String telefone;
    private String email;
    private LocalDate dataMatricula;
    private Plano planoAtivo; // Relacionamento 1:N (Chave estrangeira id_plano vira referência de objeto)

    // Construtor completo para carregar ou criar dados de alunos
    public Aluno(int id, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, LocalDate dataMatricula, Plano planoAtivo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.dataMatricula = dataMatricula;
        this.planoAtivo = planoAtivo;
    }

    // Função que calcula a data de vencimento com base na duração do plano associado
    public LocalDate calcularVencimentoPlano() {
        if (this.planoAtivo != null) {
            return this.dataMatricula.plusMonths(this.planoAtivo.getDuracaoMeses());
        }
        return null;
    }

    // Função booleana que valida se o plano está ativo na data atual
    public boolean isPlanoAtivo() {
        LocalDate vencimento = calcularVencimentoPlano();
        return vencimento != null && LocalDate.now().isBefore(vencimento);
    }

    // Métodos Getters e Setters de encapsulamento
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }

    public Plano getPlanoAtivo() { return planoAtivo; }
    public void setPlanoAtivo(Plano planoAtivo) { this.planoAtivo = planoAtivo; }
}