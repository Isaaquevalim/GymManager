package dao;

import entities.Aula;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO {

    public void salvar(Aula aula) {
        // Vincula duas chaves estrangeiras: a sala onde vai ocorrer e o instrutor responsável
        String sql = "INSERT INTO aula (numero_sala, id_funcionario, tipo_aula, data_aula, horario_aula) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, aula.getSala().getNumeroSala());
            stmt.setInt(2, aula.getFuncionario().getIdFuncionario());
            stmt.setString(3, aula.getTipoAula());
            stmt.setDate(4, Date.valueOf(aula.getDataAula()));
            // Converte LocalTime do Java para Time do PostgreSQL
            stmt.setTime(5, Time.valueOf(aula.getHorarioAula()));

            stmt.executeUpdate();
            System.out.println("✅ Aula agendada com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar aula: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // Método encapsulado para o bloco try-catch de segurança
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