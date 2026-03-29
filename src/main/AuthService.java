package main;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    private static final String LOGIN_SQL = """
            SELECT username, full_name, role, password, is_active
            FROM users
            WHERE username = ?
            """;

    public AuthResult authenticate(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return AuthResult.failure("Enter your username and password.");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(LOGIN_SQL)) {

            statement.setString(1, username.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return AuthResult.failure("Invalid username or password.");
                }

                boolean active = resultSet.getBoolean("is_active");
                if (!active) {
                    return AuthResult.failure("This account is inactive.");
                }

                String storedPassword = resultSet.getString("password");
                if (!password.equals(storedPassword)) {
                    return AuthResult.failure("Invalid username or password.");
                }

                return AuthResult.success(
                        resultSet.getString("username"),
                        resultSet.getString("full_name"),
                        resultSet.getString("role")
                );
            }
        } catch (SQLException ex) {
            return AuthResult.failure("Database error: " + ex.getMessage());
        }
    }
}
