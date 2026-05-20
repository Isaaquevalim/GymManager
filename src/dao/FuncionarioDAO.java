package dao;

import entities.Funcionario;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void salvar(Funcionario funcionario) {
        // O id_funcionario não é enviado no INSERT porque é SERIAL (Gerado automaticamente pelo PostgreSQL)
        String sql = "INSERT INTO funcionario (cargo, salario, pontos_acesso, delegacoes) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, funcionario.getCargo());
            stmt.setDouble(2, funcionario.getSalario());
            stmt.setString(3, funcionario.getPontosAcesso());
            stmt.setString(4, funcionario.getDelegacoes());

            stmt.executeUpdate();
            System.out.println("✅ Funcionário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar funcionário: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public List<Funcionario> listarTodos() {
        String sql = "SELECT * FROM funcionario";
        List<Funcionario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionario func = new Funcionario(
                        rs.getInt("id_funcionario"),
                        rs.getString("cargo"),
                        rs.getDouble("salario"),
                        rs.getString("pontos_acesso"),
                        rs.getString("delegacoes")
                );
                lista.add(func);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar funcionários: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }

    public void deletar(int idFuncionario) {
        String sql = "DELETE FROM funcionario WHERE id_funcionario = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idFuncionario);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Funcionário desligado com sucesso!");
            } else {
                System.out.println("⚠️ Nenhum funcionário encontrado com este ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar funcionário: " + e.getMessage());
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