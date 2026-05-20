package entities;

// Objetivo: Controlar o patrimônio da academia, sua localização física e integridade.
public class Equipamento {

    private int idEquipamento;
    private Sala sala; // Associação: Indica em qual espaço físico a máquina está localizada.
    private String tipoEquipamento;
    private int quantidadeEquipamento;
    private String statusEquipamento; // Armazena estados operacionais (ex: Ativo, Em Manutenção).

    public Equipamento(int idEquipamento, Sala sala, String tipoEquipamento, int quantidadeEquipamento, String statusEquipamento) {
        this.idEquipamento = idEquipamento;
        this.sala = sala;
        this.tipoEquipamento = tipoEquipamento;
        this.quantidadeEquipamento = quantidadeEquipamento;
        this.statusEquipamento = statusEquipamento;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(int idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public String getTipoEquipamento() {
        return tipoEquipamento;
    }

    public void setTipoEquipamento(String tipoEquipamento) {
        this.tipoEquipamento = tipoEquipamento;
    }

    public int getQuantidadeEquipamento() {
        return quantidadeEquipamento;
    }

    public void setQuantidadeEquipamento(int quantidadeEquipamento) {
        this.quantidadeEquipamento = quantidadeEquipamento;
    }

    public String getStatusEquipamento() {
        return statusEquipamento;
    }

    public void setStatusEquipamento(String statusEquipamento) {
        this.statusEquipamento = statusEquipamento;
    }
}