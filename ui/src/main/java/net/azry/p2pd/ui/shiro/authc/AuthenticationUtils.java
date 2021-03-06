package net.azry.p2pd.ui.shiro.authc;

import net.azry.p2pd.core.Config;
import net.azry.p2pd.ui.shiro.P2pdJdbcRealm;
import net.azry.p2pd.ui.User;
import net.azry.p2pd.ui.shiro.authz.Roles;
import net.azry.p2pd.ui.database.DatabaseConnectionFactory;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.Shiro1CryptFormat;
import org.apache.shiro.util.SimpleByteSource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationUtils {
	static boolean isInitialized  = false;

	public static String generateRandomPlainPassword(int length) {
		return new BigInteger(250, new SecureRandom()).toString(32).substring(0, length - 1);
	}

	public static String[] generateSaltedHashedPassword(String password) {
		String publicSalt = new BigInteger(250, new SecureRandom()).toString(32);

		HashService hashService = P2pdJdbcRealm.getHashService();
		HashRequest request = new HashRequest.Builder().setSalt(new SimpleByteSource(publicSalt)).setSource(new SimpleByteSource(password)).build();
		HashFormat formatter = new Shiro1CryptFormat();

		Hash hash = hashService.computeHash(request);
		String hashedPassword = formatter.format(hash);

		String[] result = new String[2];
		result[0] = hashedPassword;
		result[1] = publicSalt;

		return result;
	}

	public static void initialize() {
		if (isInitialized)  {
			return;
		}
		try {
			Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
			String query = "select 1 from users;";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			isInitialized = resultSet.next();
			resultSet.close();
			statement.close();
			if (isInitialized) {
				return;
			}

			String displayName = Config.properties.get("authentication.default_admin");
			String email = Config.properties.get("authentication.default_admin_mail");
			int passLength = Integer.parseInt(Config.properties.get("authentication.default_admin_password_length"));
			String password = generateRandomPlainPassword(passLength);

			User admin = new User();
			admin.setDisplayName(displayName);
			admin.setEmail(email);
			admin.register(password);
			admin.addRole(Roles.USER);
			admin.addRole(Roles.ADMIN);

			System.out.println("Created new admin user " + displayName + " with password " + password);
			isInitialized = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
