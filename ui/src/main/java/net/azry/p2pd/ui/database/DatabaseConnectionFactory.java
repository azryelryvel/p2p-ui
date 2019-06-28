package net.azry.p2pd.ui.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
    public static Connection getDatabaseConnection() throws SQLException {
        DataSource source = DataSourceFactory.createMysqlDatasource();
        return source.getConnection();
    }
}
