package dao;

import entities.Aluno;
import entities.Matricula;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {

    public void salvar(Matricula matricula) {
        // Recebe os dados do plano e insere referenciando o CPF do aluno
        String sql = "INSERT INTO matricula (cpf_aluno, tipo_plano, valor_plano, data_inicio, data_fim, permissoes) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            // Aqui extraímos a chave estrangeira (CPF) de dentro do objeto Aluno associado à matrícula
            stmt.setString(1, matricula.getAluno().getCpfAluno());
            stmt.setString(2, matricula.getTipoPlano());
            stmt.setDouble(3, matricula.getValorPlano());
            stmt.setDate(4, Date.valueOf(matricula.getDataInicio()));
            stmt.setDate(5, Date.valueOf(matricula.getDataFim()));
            stmt.setString(6, matricula.getPermissoes());

            stmt.executeUpdate();
            System.out.println("✅ Matrícula/Plano registrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar matrícula: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public List<Matricula> listarTodas() {
        String sql = "SELECT * FROM matricula";
        List<Matricula> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // Instanciamos o AlunoDAO para buscar o objeto Aluno completo usando o CPF salvo na matrícula
        AlunoDAO alunoDAO = new AlunoDAO();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String cpfAluno = rs.getString("cpf_aluno");
                // ATENÇÃO EQUIPE: Para isso funcionar 100%, você precisará criar um método 'buscarPorCpf' no AlunoDAO depois!
                // Al