package dao;

// Importamos as classes de modelo e a nossa classe de conexão, além das ferramentas do JDBC.
import entities.Aluno;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // --- MÉTODO SALVAR (INSERT) ---
    // Este método pega num objeto 'Aluno' que está na memória do Java e insere-o como uma nova linha na tabela do PostgreSQL.
    public void salvar(Aluno aluno) {
        // A query SQL utiliza os pontos de interrogação (?) para evitar SQL Injection.
        // Repara que não inserimos o 'id_aluno' aqui, pois o PostgreSQL vai gerá-lo automaticamente através do SERIAL.
        String sql = "INSERT INTO aluno (nome, cpf, data_nascimento, telefone, email, data_matricula, id_plano) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 1. Abre a ligação com o banco de dados
            conn = DatabaseConnection.getConnection();

            // 2. Prepara a execução da query e preenche cada parâmetro na ordem correta
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());

            // Convertemos o LocalDate do Java para o formato Date exigido pelo SQL.
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));

            stmt.setString(4, aluno.getTelefone());
            stmt.setString(5, aluno.getEmail());
            stmt.setDate(6, Date.valueOf(aluno.getDataMatricula()));

            // 3. Validação da Chave Estrangeira: Se o aluno tiver um plano ativo, guardamos o ID desse plano.
            if (aluno.getPlanoAtivo() != null) {
                // Usamos o getIdPlano() que atualizámos na classe entities.Plano
                stmt.setInt(7, aluno.getPlanoAtivo().getIdPlano());
            } else {
                // Se o aluno não tiver plano associado, inserimos um valor NULL no banco de dados.
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            // 4. Executa o comando de inserção no PostgreSQL
            stmt.executeUpdate();
            System.out.println("Aluno registado com sucesso no banco de dados!");

        } catch (SQLException e) {
            System.out.println("Erro ao guardar aluno: " + e.getMessage());
        } finally {
            // O bloco finally garante que, independentemente de correr bem ou haver um erro,
            // a ligação com o banco de dados será fechada para não sobrecarregar o PostgreSQL.
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a ligação: " + e.getMessage());
            }
        }
    }

    // --- MÉTODO LISTAR TODOS (SELECT) ---
    // Vai buscar todas as linhas da tabela 'aluno', reconstrói os objetos no Java e devolve uma lista completa.
    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> listaAlunos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // Guarda as linhas retornadas pela consulta SQL

        // Instanciamos o PlanoDAO para carregar os dados completos do plano de cada aluno.
        PlanoDAO planoDAO = new PlanoDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(); // Executa a consulta de leitura

            while (rs.next()) {
                // Pega no ID do plano armazenado na coluna 'id_plano' desta linha
                int idPlano = rs.getInt("id_plano");

                // Faz uma consulta à tabela de planos para obter o objeto Plano completo
                Plano plano = planoDAO.buscarPorId(idPlano);

                // Constrói o objeto Aluno apontando para a coluna correta 'id_aluno' (exigência do Dicionário de Dados)
                Aluno aluno = new Aluno(
                        rs.getInt("id_aluno"), // -> Atualizado conforme a imagem do Dicionário de Dados
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento").toLocalDate(), // Converte de SQL Date para LocalDate do Java
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getDate("data_matricula").toLocalDate(),
                        plano // Associa o plano completo ao aluno
                );

                listaAlunos.add(aluno);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar ligações: " + e.getMessage());
            }
        }
        return listaAlunos;
    }

    // --- MÉTODO BUSCAR POR ID (SELECT ESPECÍFICO) ---
    // Procura por um único aluno utilizando a sua Chave Primária específica.
    // Este método é o que o vosso InscricaoService utiliza para aplicar as regras de validação.
    public Aluno buscarPorId(int idAluno) {
        // A consulta faz o filtro utilizando a coluna 'id_aluno'
        String sql = "SELECT * FROM aluno WHERE id_aluno = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PlanoDAO planoDAO = new PlanoDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAluno); // Substitui o ponto de interrogação pelo ID solicitado
            rs = stmt.executeQuery();

            // Se o banco de dados encontrar o registo, monta o objeto Aluno
            if (rs.next()) {
                int idPlano = rs.getInt("id_plano");
                Plano plano = planoDAO.buscarPorId(idPlano);

                return new Aluno(
                        rs.getInt("id_aluno"), // -> Captura o ID da coluna correta
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getDate("data_matricula").toLocalDate(),
                        plano
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno por ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar ligações: " + e.getMessage());
            }
        }
        return null; // Retorna null se o aluno com esse ID não existir no PostgreSQL
    }
}