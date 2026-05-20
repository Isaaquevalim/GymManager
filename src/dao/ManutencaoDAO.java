package dao;

import entities.Manutencao;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManutencaoDAO {

    public void salvar(Manutencao manutencao) {
        String sql = "INSERT INTO manutencao (id_equipamento, data_manutencao, valor_manutencao) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, manutencao.getEquipamento().getIdEquipamento());
            stmt.setDate(2, Date.valueOf(manutencao.getDataManutencao()));
            stmt.setDouble(3, manutencao.getValorManutencao());

            stmt.executeUpdate();
            System.out.println("✅ Ordem de serviço de manutenção registrada!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao registrar manutenção: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- MÉTODOS PARA O FLUXO 7 (FINANCEIRO) ---
    // Soma todo o dinheiro gasto consertando equipamentos
    public double somarCustosManutencao() {
        String sql = "SELECT SUM(valor_manutencao) FROM manutencao";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1); // Retorna o valor da soma
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao calcular custos de manutenção: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return 0.0;
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