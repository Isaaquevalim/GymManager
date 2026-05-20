package entities;

import java.time.LocalDate;

// Objetivo: Controlar contas operacionais externas e custos internos (como a folha salarial dos funcionários).
// Regra de Negócio: Os atributos 'manutencao' e 'funcionario' podem ser nulos caso a conta seja de outra natureza (ex: luz, água).
public class ServicosExternos {

    private int idServico;
    private Manutencao manutencao; // Associação opcional com uma Ordem de Serviço.
    private Funcionario funcionario; // Associação opcional com um Colaborador (usado para registrar a folha salarial).
    private String tipoServico;
    private double valorServico;
    private LocalDate dataVencimento;
    private String statusPagamento;

    public ServicosExternos(int idServico, Manutencao manutencao, Funcionario funcionario, String tipoServico, double valorServico, LocalDate dataVencimento, String statusPagamento) {
        this.idServico = idServico;
        this.manutencao = manutencao;
        this.funcionario = funcionario;
        this.tipoServico = tipoServico;
        this.valorServico = valorServico;
        this.dataVencimento = dataVencimento;
        this.statusPagamento = statusPagamento;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public Manutencao getManutencao() {
        return manutencao;
    }

    public void setManutencao(Manutencao manutencao) {
        this.manutencao = manutencao;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public double getValorServico() {
        return valorServico;
    }

    public void setValorServico(double valorServico) {
        this.valorServico = valorServico;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}