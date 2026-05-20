package entities;

// Objetivo: Armazenar dados do corpo de funcionários e corpo técnico (instrutores).
// Regra de Banco: O 'idFuncionario' espelha a chave primária SERIAL 'id_funcionario'.
public class Funcionario {

    private int idFuncionario;
    private String cargo;
    private double salario;
    private String pontosAcesso;
    private String delegacoes;

    public Funcionario(int idFuncionario, String cargo, double salario, String pontosAcesso, String delegacoes) {
        this.idFuncionario = idFuncionario;
        this.cargo = cargo;
        this.salario = salario;
        this.pontosAcesso = pontosAcesso;
        this.delegacoes = delegacoes;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getPontosAcesso() {
        return pontosAcesso;
    }

    public void setPontosAcesso(String pontosAcesso) {
        this.pontosAcesso = pontosAcesso;
    }

    public String getDelegacoes() {
        return delegacoes;
    }

    public void setDelegacoes(String delegacoes) {
        this.delegacoes = delegacoes;
    }
}