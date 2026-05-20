package dao;

import entities.Pagamento;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagamentoDAO {

    public void salvar(Pagamento pagamento) {
        String sql = "INSERT INTO pagamento (id_matricula, data_pagamento, valor_pagamento, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, pagamento.getMatricula().getIdMatricula());
            stmt.setDate(2, Date.valueOf(pagamento.getDataPagamento()));
            stmt.setDouble(3, pagamento.getValorPagamento());
            stmt.setString(4, pagamento.getStatus());

            stmt.executeUpdate();
            System.out.println("✅ Pagamento da mensalidade processado com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao registrar pagamento: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- MÉTODOS PARA O FLUXO 7 (FINANCEIRO) ---
    // Soma todo o dinheiro que entrou na academia (Apenas status 'Pago')
    public double somarReceitasTotais() {
        String sql = "SELECT SUM(valor_pagamento) FROM pagamento WHERE status = 'Pago'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao calcular receitas: " + e.getMessage());
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