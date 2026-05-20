package dao;

import entities.Aula;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AulaDAO {

    // Salva uma nova aula vinculando a chave estrangeira do instrutor
    public void salvar(Aula aula) {
        String sql = "INSERT INTO aula (nome, descricao, capacidade_maxima, horario, duracao_minutos, id_instrutor) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, aula.getNome());
            stmt.setString(2, aula.getDescricao());
            stmt.setInt(3, aula.getCapacidadeMaxima());
            // Converte o LocalDateTime do Java para o Timestamp aceito pelo PostgreSQL
            stmt.setTimestamp(4, Timestamp.valueOf(aula.getHorario()));
            stmt.setInt(5, aula.getDuracaoMinutos());
            stmt.setInt(6, aula.getInstrutor().getId());

            stmt.executeUpdate();
            System.out.println("Aula coletiva criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar aula: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    // Conta quantos alunos estão matriculados em uma aula específica (Regra de Negócio Complexa)
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
                return rs.getInt(1); // Retorna a contagem da primeira coluna resultante
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar inscritos: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return 0;
    }

    // Efetiva a inscrição do aluno na tabela intermediária N:M
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
            System.out.println("Inscrição registrada no banco de dados!");
        } catch (SQLException e) {
            System.out.println("Erro ao vincular aluno à aula: " + e.getMessage());
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
            System.out.println("Erro ao fechar conexões: " + e.getMessage());
        }
    }

    // Verifica se o aluno já tem aula naquele exato horário
    public boolean verificarConflitoHorario(int idAluno, java.time.LocalDateTime horarioDesejado) {
        // Query que cruza a tabela intermediária com a tabela de aulas para checar o horário
        String sql = "SELECT a.id FROM aula a INNER JOIN InscricaoAula ia ON a.id = ia.id_aula WHERE ia.id_aluno = ? AND a.horario = ?";
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        java.sql.ResultSet rs = null;

        try {
            conn = database.DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(horarioDesejado));
            rs = stmt.executeQuery();

            // Se o ResultSet tiver alguma linha, significa que há conflito
            return rs.next();
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao verificar conflitos de horário: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (java.sql.SQLException e) {
                System.out.println("Erro ao fechar conexão.");
            }
        }
        return false;
    }

    // Adicione este método dentro da classe AulaDAO
    public Aula buscarPorId(int id) {
        String sql = "SELECT * FROM aula WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // Instanciamos o InstrutorDAO para carregar o instrutor da aula
        InstrutorDAO instrutorDAO = new InstrutorDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Recupera a chave estrangeira do instrutor [cite: 11, 79]
                int idInstrutor = rs.getInt("id_instrutor");
                // Busca o objeto Instrutor completo correspondente
                entities.Instrutor instrutor = instrutorDAO.buscarPorId(idInstrutor);

                // Retorna a Aula com a referência do objeto Instrutor corretamente resolvida [cite: 11]
                return new Aula(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("capacidade_maxima"),
                        rs.getTimestamp("horario").toLocalDateTime(), // Converte Timestamp do banco para LocalDateTime
                        rs.getInt("duracao_minutos"),
                        instrutor
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aula por ID: " + e.getMessage());
        } finally {
            // Liberação obrigatória dos recursos de banco [cite: 156]
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexões: " + e.getMessage());
            }
        }
        return null;
    }
}