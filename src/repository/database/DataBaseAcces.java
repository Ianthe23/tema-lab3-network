package repository.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseAcces {
    private Connection connection;
    private String url;
    private String password;
    private String username;

    public DataBaseAcces(String url, String username, String password) {
        this.url = url;
        this.password = password;
        this.username = username;
    }

    public void createConnection() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public PreparedStatement createStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void closeStatement(PreparedStatement statement) throws SQLException {
        statement.close();
    }
}
