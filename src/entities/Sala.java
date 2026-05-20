package entities;

// Objetivo: Representar os espaços físicos da academia (Musculação, Pilates, Spinning, etc).
public class Sala {

    private int numeroSala;
    private String tipoSala;
    private int capacidadeMaxima;

    public Sala(int numeroSala, String tipoSala, int capacidadeMaxima) {
        this.numeroSala = numeroSala;
        this.tipoSala = tipoSala;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(int numeroSala) {
        this.numeroSala = numeroSala;
    }

    public String getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(String tipoSala) {
        this.tipoSala = tipoSala;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }
}