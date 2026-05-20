package dao;

import entities.Instrutor;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InstrutorDAO {

    // Insere um novo instrutor no banco de dados
    public void salvar(Instrutor instrutor) {
        String sql = "INSERT INTO instrutor (nome, cpf, telefone, especialidade, horarios_trabalho) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, instrutor.getNome());
            stmt.setString(2, instrutor.getCpf());
            stmt.setString(3, instrutor.getTelefone());
            stmt.setString(4, instrutor.getEspecialidade());
            stmt.setString(5, instrutor.getHorariosTrabalho());

            stmt.executeUpdate();
            System.out.println("Instrutor salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar instrutor: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // Busca um instrutor por ID (necessário para vincular à criação de Aulas)
    public Instrutor buscarPorId(int id) {
        String sql = "SELECT * FROM instrutor WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Instrutor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("especialidade"),
                        rs.getString("horarios_trabalho")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar instrutor: " + e.getMessage());
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