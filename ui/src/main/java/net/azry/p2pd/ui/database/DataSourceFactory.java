package net.azry.p2pd.ui.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import net.azry.p2pd.core.Config;

import javax.sql.DataSource;


public class DataSourceFactory {
    public static DataSource source;

    public static synchronized DataSource createMysqlDatasource() {
        if (source == null) {
            String datasourceServer = Config.properties.get("datasource.server");
            String datasourceDatabase = Config.properties.get("datasource.database");
            String datasourceUser = Config.properties.get("datasource.user");
            String datasourcePassword = Config.properties.get("datasource.password");

            MysqlDataSource source = new MysqlDataSource();
            source.setServerName(datasourceServer);
            source.setDatabaseName(datasourceDatabase);
            source.setUser(datasourceUser);
            source.setPassword(datasourcePassword);
            source.setURL("jdbc:mysql://" + datasourceServer + "/" + datasourceDatabase);

            DataSourceFactory.source = source;
        }

        return DataSourceFactory.source;
    }
}
