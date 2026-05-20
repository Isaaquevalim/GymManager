package dao;

import entities.Frequencia;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FrequenciaDAO {

    public void salvar(Frequencia frequencia) {
        // Resolve a tabela associativa ligando o CPF do aluno e o ID da aula
        String sql = "INSERT INTO frequencia (cpf_aluno, id_aula, quantidade) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            // Puxamos as chaves estrangeiras de dentro dos objetos associados
            stmt.setString(1, frequencia.getAluno().getCpfAluno());
            stmt.setInt(2, frequencia.getAula().getIdAula());
            stmt.setInt(3, frequencia.getQuantidade());

            stmt.executeUpdate();
            System.out.println("✅ Check-in na aula realizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao registrar frequência: " + e.getMessage());
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