package dao;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FrequenciaDAO {

    // Registra a entrada de um aluno salvando o momento exato do acesso
    public void registrarEntrada(int idAluno) {
        String sql = "INSERT INTO frequencia (id_aluno, data_hora_entrada) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Captura o instante atual

            stmt.executeUpdate();
            System.out.println("Entrada do aluno registrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar frequência: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // Conta o total de visitas de um aluno (exigência da visualização de detalhes do aluno)
    public int contarVisitasAluno(int idAluno) {
        String sql = "SELECT COUNT(*) FROM frequencia WHERE id_aluno = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar visitas: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return 0;
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