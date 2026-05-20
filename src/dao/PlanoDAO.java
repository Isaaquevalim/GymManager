package dao;

import entities.Plano;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanoDAO {

    // Salva um novo plano no banco de dados
    public void salvar(Plano plano) {
        String sql = "INSERT INTO plano (nome, descricao, valor_mensal, duracao_meses, beneficios) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection(); // Abre a conexão com o PostgreSQL
            stmt = conn.prepareStatement(sql); // Prepara a query SQL

            // Substitui as interrogações (?) pelos valores reais do objeto
            stmt.setString(1, plano.getNome());
            stmt.setString(2, plano.getDescricao());
            stmt.setDouble(3, plano.getValorMensal());
            stmt.setInt(4, plano.getDuracaoMeses());
            stmt.setString(5, plano.getBeneficios());

            stmt.executeUpdate(); // Executa o comando de inserção no banco
            System.out.println("Plano salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar plano: " + e.getMessage());
        } finally {
            // Garante o fechamento dos recursos para evitar vazamento de memória
            fecharRecursos(conn, stmt, null);
        }
    }

    // Busca um plano específico pelo ID (necessário para associar ao Aluno)
    public Plano buscarPorId(int id) {
        String sql = "SELECT * FROM plano WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery(); // Executa a consulta e armazena o resultado

            if (rs.next()) {
                // Instancia e retorna o objeto Plano preenchido com os dados do banco
                return new Plano(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("valor_mensal"),
                        rs.getInt("duracao_meses"),
                        rs.getString("beneficios")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar plano por ID: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    // Método utilitário interno para fechar as conexões de forma segura
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