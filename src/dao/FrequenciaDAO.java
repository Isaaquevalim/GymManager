package dao;

// Importamos a nossa ligação à base de dados e as classes necessárias para manipular o tempo e o SQL
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FrequenciaDAO {

    // --- REGISTAR ENTRADA (INSERT) ---
    // Este método é chamado assim que o aluno passa na catraca da academia.
    public void registrarEntrada(int idAluno) {

        // A query utiliza 'id_aluno' conforme o vosso Dicionário de Dados oficial.
        // Reparem que não enviamos o 'id_frequencia' porque o PostgreSQL gera-o automaticamente (tipo SERIAL).
        String sql = "INSERT INTO frequencia (id_aluno, data_hora_entrada) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 1. Abre a ligação à base de dados
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            // 2. Preenche o primeiro parâmetro com o ID do aluno que acabou de entrar
            stmt.setInt(1, idAluno);

            // 3. Captura o momento exato do sistema (agora) utilizando LocalDateTime.
            // O comando Timestamp.valueOf faz a tradução da data/hora do Java para a data/hora do PostgreSQL.
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            // 4. Executa a inserção
            stmt.executeUpdate();
            System.out.println("Entrada registada com sucesso na catraca!");

        } catch (SQLException e) {
            System.out.println("Erro ao registar a frequência: " + e.getMessage());
        } finally {
            // Bloqueio de segurança para fechar os recursos independentemente do que aconteça
            fecharRecursos(conn, stmt, null);
        }
    }

    // --- CONTAR VISITAS (SELECT ESTATÍSTICO) ---
    // Esta função cumpre a exigência de "calcular estatísticas de frequência" do documento do projeto.
    public int contarVisitasAluno(int idAluno) {

        // A função COUNT(*) conta quantas linhas existem na tabela de frequência para aquele id_aluno específico.
        String sql = "SELECT COUNT(*) FROM frequencia WHERE id_aluno = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno);

            // Executamos a query de leitura
            rs = stmt.executeQuery();

            // Se o banco devolver um resultado, extraímos o número gerado pelo COUNT (que está na coluna 1)
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao contar as visitas: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        // Se houver algum erro de rede ou o aluno nunca tiver ido à academia, o sistema devolve 0.
        return 0;
    }

    // --- MÉTODO AUXILIAR (ENCAPSULAMENTO LOCAL) ---
    // Criámos este método privado para não ter de repetir o bloco 'try-catch' de fechar a ligação em todas as funções acima.
    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar as ligações: " + e.getMessage());
        }
    }
}