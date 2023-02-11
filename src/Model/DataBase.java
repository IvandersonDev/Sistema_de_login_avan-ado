package Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

public class DataBase { private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";

        private static final String USERNAME = "root";
        private static final String PASSWORD = "root";
        private static final int ITERATIONS = 10000;
        private static final int KEY_LENGTH = 256;

        private Connection connection;

        public DataBase() {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                throw new IllegalStateException("Error connecting to the database", e);
            }
        }

        public boolean checkCredentials(String username, char[] password) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT password_hash, salt FROM users WHERE username = ?");
                statement.setString(1, username);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    byte[] storedHash = Base64.getDecoder().decode(result.getString("password_hash"));
                    byte[] salt = Base64.getDecoder().decode(result.getString("salt"));
                    byte[] newHash = hashPassword(password, salt);

                    if (Arrays.equals(storedHash, newHash)) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Error checking user's credentials", e);
            } finally {
                // Clear the password from memory
                Arrays.fill(password, '\0');
            }
            return false;
        }

        public void createUser(String username, char[] password) {
            byte[] salt = generateSalt();
            byte[] hash = hashPassword(password, salt);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hash);

            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)");
                statement.setString(1, username);
                statement.setString(2, encodedHash);
                statement.setString(3, encodedSalt);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Error creating user", e);
            } finally {
                // Clear the password from memory
                Arrays.fill(password, '\0');
            }
            {
            }
        }
