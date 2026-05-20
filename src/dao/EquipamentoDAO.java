package dao;

import entities.Equipamento;
import entities.Sala;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO {

    public void salvar(Equipamento equipamento) {
        String sql = "INSERT INTO equipamento (numero_sala, tipo_equipamento, quantidade_equipamento, status_equipamento) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, equipamento.getSala().getNumeroSala());
            stmt.setString(2, equipamento.getTipoEquipamento());
            stmt.setInt(3, equipamento.getQuantidadeEquipamento());
            stmt.setString(4, equipamento.getStatusEquipamento());

            stmt.executeUpdate();
            System.out.println("✅ Equipamento registrado no inventário com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar equipamento: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public void atualizarStatus(int idEquipamento, String novoStatus) {
        // Método fundamental para a regra de negócio: Mudar para "Em Manutenção"
        String sql = "UPDATE equipamento SET status_equipamento = ? WHERE id_equipamento = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idEquipamento);

            int afetadas = stmt.executeUpdate();
            if(afetadas > 0) {
                System.out.println("✅ Status do equipamento atualizado para: " + novoStatus);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar status do equipamento: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Erro ao fechar conexão.");
        }
    }
}