package entities;

import java.time.LocalDate;

// Objetivo: Rastrear os recebimentos financeiros gerados a partir de um contrato (Matrícula).
public class Pagamento {

    private int idPagamento;
    private Matricula matricula; // Associação direta com a Matrícula vinculada à transação.
    private LocalDate dataPagamento;
    private double valorPagamento;
    private String status;

    public Pagamento(int idPagamento, Matricula matricula, LocalDate dataPagamento, double valorPagamento, String status) {
        this.idPagamento = idPagamento;
        this.matricula = matricula;
        this.dataPagamento = dataPagamento;
        this.valorPagamento = valorPagamento;
        this.status = status;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}