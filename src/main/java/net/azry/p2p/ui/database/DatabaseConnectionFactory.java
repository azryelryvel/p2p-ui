package net.azry.p2p.ui.database;

import net.azry.p2p.ui.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
    public static Connection getDatabaseConnection() throws SQLException {
        String url = Config.properties.get("database.url");
        String username = Config.properties.get("database.username");
        String password = Config.properties.get("database.password");

        return DriverManager.getConnection(url, username, password);
    }
}
