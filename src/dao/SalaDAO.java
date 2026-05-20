package dao;

import entities.Sala;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {

    public void salvar(Sala sala) {
        // numero_sala é a PK, então nós inserimos manualmente ao contrário dos IDs SERIAL.
        String sql = "INSERT INTO sala (numero_sala, tipo_sala, capacidade_maxima) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sala.getNumeroSala());
            stmt.setString(2, sala.getTipoSala());
            stmt.setInt(3, sala.getCapacidadeMaxima());

            stmt.executeUpdate();
            System.out.println("✅ Sala registrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao registrar sala (Verifique se o número já existe): " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public List<Sala> listarTodas() {
        String sql = "SELECT * FROM sala";
        List<Sala> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Sala sala = new Sala(
                        rs.getInt("numero_sala"),
                        rs.getString("tipo_sala"),
                        rs.getInt("capacidade_maxima")
                );
                lista.add(sala);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar salas: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        return lista;
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