package net.azry.p2p.ui.authentication;

import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.database.DatabaseConnectionFactory;
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

	public static String[] generateSaltedHashedPassword(String password) {
		String publicSalt = new BigInteger(250, new SecureRandom()).toString(32);

		HashService hashService = P2pJdbcRealm.getHashService();
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
			if (resultSet.next()) {
				isInitialized  = true;
				return;
			}
			resultSet.close();
			statement.close();

			String username = Config.properties.get("authentication.default_admin");
			String email = Config.properties.get("authentication.default_admin_mail");
			int passLength = Integer.parseInt(Config.properties.get("authentication.default_admin_password_length"));
			String password = new BigInteger(250, new SecureRandom()).toString(32).substring(0, passLength - 1);
			String[] creds = generateSaltedHashedPassword(password);

			query = "INSERT INTO users (username, email, password, password_salt) values (?, ?, ?, ?);";
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, email);
			statement.setString(3, creds[0]);
			statement.setString(4, creds[1]);
			statement.execute();
			statement.close();
			System.out.println("Created new user " + username + " with password " + password);
			isInitialized = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
