package entities;

import java.time.LocalDate;
import java.time.LocalTime;

// Objetivo: Representar a grade de horários e modalidades da academia.
// Relacionamentos em POO: Depende da existência de uma Sala e de um Funcionário (professor).
public class Aula {

    private int idAula;
    private Sala sala; // Associação: Local onde a atividade ocorre.
    private Funcionario funcionario; // Associação: Instrutor responsável pela regência.
    private String tipoAula;
    private LocalDate dataAula;
    private LocalTime horarioAula; // Atributo adicionado para suportar o fluxo de grade horária.

    public Aula(int idAula, Sala sala, Funcionario funcionario, String tipoAula, LocalDate dataAula, LocalTime horarioAula) {
        this.idAula = idAula;
        this.sala = sala;
        this.funcionario = funcionario;
        this.tipoAula = tipoAula;
        this.dataAula = dataAula;
        this.horarioAula = horarioAula;
    }

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }

    public LocalDate getDataAula() {
        return dataAula;
    }

    public void setDataAula(LocalDate dataAula) {
        this.dataAula = dataAula;
    }

    public LocalTime getHorarioAula() {
        return horarioAula;
    }

    public void setHorarioAula(LocalTime horarioAula) {
        this.horarioAula = horarioAula;
    }
}