package dao;

import entities.ServicosExternos;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ServicosExternosDAO {

    public void salvar(ServicosExternos servico) {
        String sql = "INSERT INTO servicos_externos (id_manutencao, id_funcionario, tipo_servico, valor_servico, data_vencimento, status_pagamento) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            // Regra de Negócio: id_manutencao pode ser nulo
            if (servico.getManutencao() != null) {
                stmt.setInt(1, servico.getManutencao().getIdManutencao());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            // Regra de Negócio: id_funcionario pode ser nulo
            if (servico.getFuncionario() != null) {
                stmt.setInt(2, servico.getFuncionario().getIdFuncionario());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setString(3, servico.getTipoServico());
            stmt.setDouble(4, servico.getValorServico());
            stmt.setDate(5, Date.valueOf(servico.getDataVencimento()));
            stmt.setString(6, servico.getStatusPagamento());

            stmt.executeUpdate();
            System.out.println("✅ Conta / Serviço externo registrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao registrar serviço externo: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- MÉTODOS PARA O FLUXO 7 (FINANCEIRO) ---
    // Soma todas as despesas (Luz, água, serviços terceirizados, etc)
    public double somarDespesasExternas() {
        String sql = "SELECT SUM(valor_servico) FROM servicos_externos";
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
            System.out.println("❌ Erro ao calcular despesas externas: " + e.getMessage());
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