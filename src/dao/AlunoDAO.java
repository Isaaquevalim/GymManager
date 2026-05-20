package dao;

// Importações necessárias: Classes do nosso modelo (Entities) e as ferramentas do Java para Banco de Dados (SQL).
import entities.Aluno;
import entities.Plano; // -> CORREÇÃO: Faltava importar a classe Plano!
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // --- SALVAR (INSERT) ---
    // O objetivo deste método é pegar um objeto 'Aluno' que está na memória do Java e gravá-lo definitivamente na tabela do PostgreSQL.
    public void salvar(Aluno aluno) {

        // A Query SQL. O uso das interrogações (?) é uma técnica chamada 'Prepared Statement'.
        // Isso evita que invasores tentem colocar comandos maliciosos no meio dos dados (SQL Injection).
        String sql = "INSERT INTO aluno (nome, cpf, data_nascimento, telefone, email, data_matricula, id_plano) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 1. Abre a porta de comunicação com o PostgreSQL
            conn = DatabaseConnection.getConnection();

            // 2. Prepara o caminhão de entrega (a query) e coloca as caixas (os dados do aluno) na ordem certa.
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());

            // O Java moderno usa 'LocalDate' para datas, mas o SQL antigo usa o formato 'Date'.
            // Essa linha faz a tradução de um formato para o outro.
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));

            stmt.setString(4, aluno.getTelefone());
            stmt.setString(5, aluno.getEmail());
            stmt.setDate(6, Date.valueOf(aluno.getDataMatricula()));

            // 3. Regra de Chave Estrangeira: O aluno pode ou não ter um plano no momento do cadastro.
            if (aluno.getPlanoAtivo() != null) {
                // Se ele tem plano, gravamos o ID do plano na coluna 'id_plano'
                stmt.setInt(7, aluno.getPlanoAtivo().getId());
            } else {
                // Se não tem, gravamos um valor 'NULL' no banco.
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            // 4. Dá o comando final para o banco executar a inserção
            stmt.executeUpdate();
            System.out.println("Aluno cadastrado com sucesso no banco de dados!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
        } finally {
            // CONCEITO IMPORTANTE PARA A AVALIAÇÃO: O bloco 'finally' sempre será executado, não importa se a gravação deu certo ou se o banco de dados caiu.
            // A gente usa isso para fechar as conexões ativas e evitar que o programa deixe o PostgreSQL sobrecarregado de conexões abertas fantasmas.
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    // --- LISTAR TODOS (SELECT) ---
    // Este método vai no banco, pega TODAS as linhas da tabela 'aluno', transforma cada linha em um objeto Java e devolve uma Lista cheia.
    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> listaAlunos = new ArrayList<>(); // Aqui é onde vamos guardar os alunos encontrados

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // O ResultSet é a "tabela" virtual onde o Java guarda as linhas que o banco devolveu

        // Precisamos do PlanoDAO porque a tabela de aluno só tem o ID do plano, e nós precisamos do objeto Plano inteiro.
        PlanoDAO planoDAO = new PlanoDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            // Executa a busca. O rs.executeQuery() é diferente do executeUpdate() porque ele não altera nada, só lê.
            rs = stmt.executeQuery();

            // O 'while' faz um loop passando por cada linha que o banco retornou
            while (rs.next()) {
                // Pega o ID do plano que está salvo nesta linha do aluno
                int idPlano = rs.getInt("id_plano");

                // Manda o PlanoDAO buscar os detalhes desse plano lá na tabela 'plano'
                Plano plano = planoDAO.buscarPorId(idPlano);

                // Monta o quebra-cabeça: Cria o objeto Aluno colocando os dados da linha + o objeto Plano completo
                Aluno aluno = new Aluno(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento").toLocalDate(), // Converte de volta do formato SQL para o formato Java
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getDate("data_matricula").toLocalDate(),
                        plano
                );

                // Adiciona esse aluno montadinho na nossa lista
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
                System.out.println("Erro ao fechar conexões: " + e.getMessage());
            }
        }

        // Devolve a lista pronta (mesmo que esteja vazia, se não houver alunos cadastrados)
        return listaAlunos;
    }

    // --- BUSCAR POR ID (SELECT ESPECÍFICO) ---
    // Diferente do listarTodos, este método busca exatamente um único aluno usando a sua Chave Primária (ID).
    // É essencial para a regra de negócio de matricular o aluno na aula coletiva.
    public Aluno buscarPorId(int id) {
        // A interrogação garante que vamos buscar apenas o ID que pedimos
        String sql = "SELECT * FROM aluno WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PlanoDAO planoDAO = new PlanoDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id); // Troca a '?' pelo ID que recebemos
            rs = stmt.executeQuery();

            // O 'if' verifica se o banco retornou pelo menos uma linha. Como o ID é único, se achar, é só um mesmo.
            if (rs.next()) {

                // Busca o plano completo associado a este aluno
                int idPlano = rs.getInt("id_plano");
                Plano plano = planoDAO.buscarPorId(idPlano);

                // Constrói e devolve o objeto Aluno completinho
                return new Aluno(
                        rs.getInt("id"),
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
                System.out.println("Erro ao fechar conexões: " + e.getMessage());
            }
        }

        // Se o código chegou até aqui, é porque o 'if (rs.next())' falhou (o ID não existe no banco).
        // Então devolvemos 'null' para avisar o sistema que não achamos ninguém.
        return null;
    }
}