package dao;

import entities.Aluno;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Objetivo: Isolar toda a comunicação com a tabela 'aluno' no banco de dados.
// Padrão de Projeto: Data Access Object (DAO).
public class AlunoDAO {

    // --- C: CREATE (INSERIR) ---
    public void salvar(Aluno aluno) {
        String sql = "INSERT INTO aluno (cpf_aluno, nome_completo, data_nascimento) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getCpfAluno());
            stmt.setString(2, aluno.getNomeCompleto());
            // Conversão do LocalDate do Java para o formato Date exigido pelo PostgreSQL
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));

            stmt.executeUpdate();
            System.out.println("✅ Aluno cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar aluno (Verifique se o CPF já existe): " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- R: READ (LER/LISTAR) ---
    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Reconstrói os objetos Aluno a partir das linhas do banco de dados
                Aluno aluno = new Aluno(
                        rs.getString("cpf_aluno"),
                        rs.getString("nome_completo"),
                        rs.getDate("data_nascimento").toLocalDate()
                );
                lista.add(aluno);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar alunos: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
    }

    // --- U: UPDATE (ATUALIZAR) ---
    public void atualizar(Aluno aluno) {
        // Atualiza os dados baseado na Chave Primária (CPF)
        String sql = "UPDATE aluno SET nome_completo = ?, data_nascimento = ? WHERE cpf_aluno = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getNomeCompleto());
            stmt.setDate(2, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(3, aluno.getCpfAluno());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Dados do aluno atualizados com sucesso!");
            } else {
                System.out.println("⚠️ Nenhum aluno encontrado com este CPF.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar aluno: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- D: DELETE (DELETAR) ---
    public void deletar(String cpfAluno) {
        String sql = "DELETE FROM aluno WHERE cpf_aluno = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpfAluno);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Aluno removido com sucesso!");
            } else {
                System.out.println("⚠️ Nenhum aluno encontrado com este CPF.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar aluno: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // Método encapsulado para garantir o fechamento seguro de conexões
    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Erro ao fechar conexão: " + e.getMessage());
        }
    }
}