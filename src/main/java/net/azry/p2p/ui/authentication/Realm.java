package net.azry.p2p.ui.authentication;

import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.database.DataSourceFactory;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.SimpleByteSource;

import javax.sql.DataSource;

public class Realm extends JdbcRealm {
    public Realm() {
        String privateSalt = Config.properties.get("authentication.private_salt");
        String authenticationQuery = Config.properties.get("authentication.query");
        int iterations = Integer.parseInt(Config.properties.get("authentication.hash.iterations"));
        String algorithmName = Config.properties.get("authentication.hash.algorithm");

        setSaltStyle(SaltStyle.COLUMN);
        setAuthenticationQuery(authenticationQuery);

        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashIterations(iterations);
        hashService.setHashAlgorithmName(algorithmName);
        hashService.setPrivateSalt(new SimpleByteSource(privateSalt));
        hashService.setGeneratePublicSalt(true);

        DefaultPasswordService passwordService = new DefaultPasswordService();
        passwordService.setHashService(hashService);
        PasswordMatcher matcher = new PasswordMatcher();
        matcher.setPasswordService(passwordService);
        setCredentialsMatcher(matcher);

        DataSource source = DataSourceFactory.createMysqlDatasource();
        setDataSource(source);
    }
}
