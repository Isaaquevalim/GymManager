package dao;

import entities.Aula;
import entities.Funcionario;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AulaDAO {

    // --- CRIAR AULA (INSERT) ---
    public void salvar(Aula aula) {
        // A query aponta explicitamente para a coluna id_instrutor
        String sql = "INSERT INTO aula (nome, descricao, capacidade_maxima, horario, duracao_minutos, id_instrutor) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, aula.getNome());
            stmt.setString(2, aula.getDescricao());
            stmt.setInt(3, aula.getCapacidadeMaxima());
            // Converte a data/hora do Java para o formato aceite pelo banco
            stmt.setTimestamp(4, Timestamp.valueOf(aula.getHorario()));
            stmt.setInt(5, aula.getDuracaoMinutos());

            // Aqui estava um dos grandes causadores de erro!
            // Agora chamamos getIdInstrutor() do objeto instrutor que está associado a esta aula.
            stmt.setInt(6, aula.getInstrutor().getIdInstrutor());

            stmt.executeUpdate();
            System.out.println("Aula coletiva criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar a aula: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- LER AULA (SELECT) ---
    public Aula buscarPorId(int idAula) {
        // A busca é feita através do novo nome da PK (id_aula)
        String sql = "SELECT * FROM aula WHERE id_aula = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        InstrutorDAO instrutorDAO = new InstrutorDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAula);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Recupera a chave estrangeira do banco
                int idInstrutor = rs.getInt("id_instrutor");

                // Manda o InstrutorDAO ir buscar os dados do professor
                Funcionario instrutor = instrutorDAO.buscarPorId(idInstrutor);

                // Reconstrói o objeto Aula com o ID correto e o professor associado
                return new Aula(
                        rs.getInt("id_aula"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("capacidade_maxima"),
                        rs.getTimestamp("horario").toLocalDateTime(),
                        rs.getInt("duracao_minutos"),
                        instrutor
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aula: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return null;
    }

    // --- REGRAS DE NEGÓCIO PARA A AVALIAÇÃO ---
    // Este método vai à tabela intermediária 'InscricaoAula' contar quantas matrículas existem para um id_aula.
    public int contarAlunosInscritos(int idAula) {
        String sql = "SELECT COUNT(*) FROM InscricaoAula WHERE id_aula = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAula);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar alunos inscritos: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return 0;
    }

    // Faz um cruzamento (INNER JOIN) para descobrir se o aluno já tem alguma aula no mesmo horário.
    public boolean verificarConflitoHorario(int idAluno, LocalDateTime horarioDesejado) {
        String sql = "SELECT a.id_aula FROM aula a INNER JOIN InscricaoAula ia ON a.id_aula = ia.id_aula WHERE ia.id_aluno = ? AND a.horario = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);
            stmt.setTimestamp(2, Timestamp.valueOf(horarioDesejado));
            rs = stmt.executeQuery();

            return rs.next(); // Retorna true (verdadeiro) se encontrar algum conflito!
        } catch (SQLException e) {
            System.out.println("Erro ao verificar conflito de horário: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return false;
    }

    // --- RELACIONAMENTO N:M ---
    // Grava efetivamente a matrícula na tabela intermediária (Muitos para Muitos)
    public void inscreverAluno(int idAluno, int idAula) {
        String sql = "INSERT INTO InscricaoAula (id_aluno, id_aula) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);
            stmt.setInt(2, idAula);
            stmt.executeUpdate();
            System.out.println("Matrícula na aula efetuada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inscrever aluno: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // Método auxiliar para não repetir o código de try-catch sempre que for preciso fechar uma ligação
    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar ligações: " + e.getMessage());
        }
    }
}