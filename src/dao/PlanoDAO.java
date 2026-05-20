package dao;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanoDAO {

    // Grava um novo plano no PostgreSQL
    public void salvar(Plano plano) {
        String sql = "INSERT INTO plano (nome, descricao, valor_mensal, duracao_meses, beneficios) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, plano.getNome());
            stmt.setString(2, plano.getDescricao());
            stmt.setDouble(3, plano.getValorMensal());
            stmt.setInt(4, plano.getDuracaoMeses());
            stmt.setString(5, plano.getBeneficios());

            stmt.executeUpdate();
            System.out.println("Plano guardado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar plano: " + e.getMessage());
        } finally {
            // Bloco de segurança obrigatório para fechar os recursos de rede e memória
            fecharRecursos(conn, stmt, null);
        }
    }

    // Procura um plano específico através da Chave Primária atualizada
    public Plano buscarPorId(int idPlano) {
        // ATENÇÃO EQUIPA: A query agora aponta explicitamente para 'id_plano'
        String sql = "SELECT * FROM plano WHERE id_plano = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPlano);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Criamos o objeto capturando o valor da coluna 'id_plano' conforme o Dicionário de Dados
                return new Plano(
                        rs.getInt("id_plano"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("valor_mensal"),
                        rs.getInt("duracao_meses"),
                        rs.getString("beneficios")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar plano: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexões: " + e.getMessage());
        }
    }
}