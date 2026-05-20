package entities;

import java.time.LocalDate;

// Objetivo: Registrar os custos e as datas dos reparos técnicos feitos nos equipamentos de treino.
public class Manutencao {

    private int idManutencao;
    private Equipamento equipamento; // Associação: Máquina específica que sofreu a intervenção.
    private LocalDate dataManutencao;
    private double valorManutencao;

    public Manutencao(int idManutencao, Equipamento equipamento, LocalDate dataManutencao, double valorManutencao) {
        this.idManutencao = idManutencao;
        this.equipamento = equipamento;
        this.dataManutencao = dataManutencao;
        this.valorManutencao = valorManutencao;
    }

    public int getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(int idManutencao) {
        this.idManutencao = idManutencao;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public LocalDate getDataManutencao() {
        return dataManutencao;
    }

    public void setDataManutencao(LocalDate dataManutencao) {
        this.dataManutencao = dataManutencao;
    }

    public double getValorManutencao() {
        return valorManutencao;
    }

    public void setValorManutencao(double valorManutencao) {
        this.valorManutencao = valorManutencao;
    }
}