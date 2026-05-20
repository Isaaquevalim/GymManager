package entities;

import java.time.LocalDate;

// Objetivo: Vincular contratos, valores e permissões de acesso ao objeto Aluno.
// Relacionamento em POO: Associação (Composição/Agregação) contendo o objeto Aluno completo na memória.
public class Matricula {

    private int idMatricula;
    private Aluno aluno; // Relacionamento N:1. Guarda a referência completa do Aluno associado.
    private String tipoPlano;
    private double valorPlano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String permissoes;

    public Matricula(int idMatricula, Aluno aluno, String tipoPlano, double valorPlano, LocalDate dataInicio, LocalDate dataFim, String permissoes) {
        this.idMatricula = idMatricula;
        this.aluno = aluno;
        this.tipoPlano = tipoPlano;
        this.valorPlano = valorPlano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.permissoes = permissoes;
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(String tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    public double getValorPlano() {
        return valorPlano;
    }

    public void setValorPlano(double valorPlano) {
        this.valorPlano = valorPlano;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }
}