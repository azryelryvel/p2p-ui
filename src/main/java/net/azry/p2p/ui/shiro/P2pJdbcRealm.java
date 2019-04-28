package net.azry.p2p.ui.shiro;

import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.database.DataSourceFactory;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.SimpleByteSource;

import javax.sql.DataSource;

public class P2pJdbcRealm extends JdbcRealm {
	public P2pJdbcRealm() {
		setAuthenticationQuery("select password, salt from users where uuid = ?");
		setUserRolesQuery("select role from roles where uuid = ?");
		setPermissionsQuery("select permission from permissions where role = ?");

		setSaltStyle(SaltStyle.COLUMN);

		PasswordMatcher matcher = new PasswordMatcher();
		matcher.setPasswordService(getPasswordService());
		setCredentialsMatcher(matcher);

		DataSource source = DataSourceFactory.createMysqlDatasource();
		setDataSource(source);
	}

	public static PasswordService getPasswordService() {
		DefaultPasswordService passwordService = new DefaultPasswordService();
		passwordService.setHashService(getHashService());

		return passwordService;

	}
	public static HashService getHashService() {
		String privateSalt = Config.properties.get("authentication.private_salt");
		String algorithmName = Config.properties.get("authentication.hash.algorithm");
		int iterations = Integer.parseInt(Config.properties.get("authentication.hash.iterations"));

		DefaultHashService hashService = new DefaultHashService();
		hashService.setHashIterations(iterations);
		hashService.setHashAlgorithmName(algorithmName);
		hashService.setPrivateSalt(new SimpleByteSource(privateSalt));
		hashService.setGeneratePublicSalt(true);

		return hashService;
	}
}
