package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Constantes com as credenciais do seu banco local
    private static final String URL = "jdbc:postgresql://localhost:5432/Gym_Manager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123"; // senha de acesso do pgAdmin

    // Método estático para retornar a conexão. Não precisa instanciar a classe para usá-lo[cite: 133].
    public static Connection getConnection() throws SQLException {
        // Tenta estabelecer a conexão com os dados fornecidos
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}