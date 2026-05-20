package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.DatabaseConnection;

public class InscricaoDAO {

    // Persiste a inscrição no banco de dados
    public void salvarInscricao(int idAluno, int idAula) {
        String sql = "INSERT INTO InscricaoAula (id_aluno, id_aula) VALUES (?, ?)"; // Relacionamento N:M
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Simulação de abertura de conexão (sua classe de Database)
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);
            stmt.setInt(2, idAula);
            stmt.executeUpdate(); // Executa o insert no PostgreSQL

        } catch (SQLException e) {
            System.out.println("Erro ao gravar no BD: " + e.getMessage());
        } finally {
            // O bloco finally garante a execução desta parte de qualquer forma
            // Essencial para fechar a conexão com o banco e evitar travamentos na aplicação
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão.");
            }
        }
    }
}