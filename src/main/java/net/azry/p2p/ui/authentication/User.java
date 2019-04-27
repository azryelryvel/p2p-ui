package net.azry.p2p.ui.authentication;

import net.azry.p2p.ui.authentication.exceptions.UserNotAuthenticatedException;
import net.azry.p2p.ui.database.DatabaseConnectionFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.Shiro1CryptFormat;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;

import java.security.InvalidParameterException;
import java.sql.*;

import java.math.BigInteger;
import java.security.SecureRandom;

// TODO : logs instead of println, move security parts to other class

public class User {
	private String username;

	private String email;

	public User(String username) {
		this.username = username;
	}

	public User(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public boolean login(String password){
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject currentUser = SecurityUtils.getSubject();

		try {
			currentUser.login(token);
		} catch (IllegalStateException e) {
			System.out.println("illegal");
		} catch (UnknownAccountException uae ) {
			System.out.println("unknown");
		} catch (IncorrectCredentialsException ice ) {
			System.out.println("incorrect");
		} catch (LockedAccountException lae ) {
			System.out.println("locked");
		} catch(AuthenticationException aex){
			System.out.println("auth");
		}

		return currentUser.isAuthenticated();
	}

	public void register(String password) throws SQLException, InvalidParameterException {
		if (username == null || email == null) {
			throw new InvalidParameterException("username or email cannot be null during a registration");
		}

		String[] creds = AuthenticationUtils.generateSaltedHashedPassword(password);

		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();

		String query = "INSERT INTO users (username, email, password, password_salt) VALUES (?, ?, ?, ?);";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, username);
		statement.setString(2, email);
		statement.setString(3, creds[0]);
		statement.setString(4, creds[1]);

		System.out.println("registering user " + username + " with hash " + creds[0] + "and salt " + creds[1]);
		statement.execute();
		statement.close();
	}

	public static User getCurrent() throws UserNotAuthenticatedException {
		String username;
		User user;

		Subject currentSubject = SecurityUtils.getSubject();

		if (currentSubject.isAuthenticated()) {
			username = currentSubject.getPrincipal().toString();
			user = new User(username);
		} else {
			throw new UserNotAuthenticatedException();
		}

		try {
			Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
			String query = "select email from users where username = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			user.email = resultSet.getString(0);
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			user.email = "INVALID";
			e.printStackTrace();
		}
		return  user;
	}
}
