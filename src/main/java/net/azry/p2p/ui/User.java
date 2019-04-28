package net.azry.p2p.ui;

import com.vaadin.flow.component.UI;
import net.azry.p2p.ui.shiro.authc.AuthenticationUtils;
import net.azry.p2p.ui.exceptions.UsernameAlreadyExistsException;
import net.azry.p2p.ui.database.DatabaseConnectionFactory;
import net.azry.p2p.ui.shiro.authz.Roles;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.UUID;

// TODO : logs instead of println, move security parts to other class

public class User {
	public String uuid;

	public String displayName;

	public String email;

	public User() {
		uuid = UUID.randomUUID().toString();
	}

	public User(String uuid) {
		this.uuid = uuid;
	}

	public static User getByDisplayName(String displayName) throws SQLException, UnknownAccountException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "SELECT uuid FROM users WHERE display_name = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, displayName);
		ResultSet rs = statement.executeQuery();
		boolean userExists = rs.next();
		if (!userExists) {
			throw new UnknownAccountException();
		}

		String uuid = rs.getString(1);
		User user = new User(uuid);
		user.displayName = displayName;
		return user;
	}

	public boolean login(String password) {
		if (uuid == null || password == null) {
			return false;
		}

		UsernamePasswordToken token = new UsernamePasswordToken(uuid, password);
		Subject currentUser = SecurityUtils.getSubject();

		try {
			currentUser.login(token);
		} catch (AuthenticationException ignored)  {}

		return currentUser.isAuthenticated();
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
		UI.getCurrent().getPage().reload();
	}

	public void register(String password) throws SQLException, InvalidParameterException {
		if (displayName == null || email == null) {
			throw new InvalidParameterException("displayName and email cannot be null during a registration");
		}

		String[] creds = AuthenticationUtils.generateSaltedHashedPassword(password);

		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "INSERT INTO users (uuid, display_name, email, password, password_salt) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.setString(2, displayName);
		statement.setString(3, email);
		statement.setString(4, creds[0]);
		statement.setString(5, creds[1]);

		statement.execute();
		statement.close();
	}

	public void addRole(Roles role) throws SQLException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();

		String query = "SELECT 1 FROM user_roles WHERE role_name = ? AND uuid = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, role.label);
		statement.setString(2, uuid);
		ResultSet rs = statement.executeQuery();
		boolean alreadyHasRole = rs.next();
		rs.close();
		statement.close();
		if (alreadyHasRole) {
			return;
		}

		query = "INSERT INTO user_roles (uuid, role_name) values (?, ?);";
		statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.setString(2, role.label);
		statement.execute();
		statement.close();
	}

	public void updatePassword(String password) throws SQLException {
		String[] newCreds = AuthenticationUtils.generateSaltedHashedPassword(password);
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "UPDATE users SET password = ?, password_salt = ? WHERE uuid = ?;";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, newCreds[0]);
		statement.setString(2, newCreds[1]);
		statement.setString(3, this.uuid);
		statement.execute();
		statement.close();
	}

	public void updateUsername(String username) throws SQLException, UsernameAlreadyExistsException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "SELECT 1 FROM users WHERE display_name = ?;";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, username);
		ResultSet rs = statement.executeQuery();
		boolean userExists = rs.next();
		rs.close();
		statement.close();
		if (userExists) {
			throw new UsernameAlreadyExistsException("Username " + username + " already exists");
		}

		query = "UPDATE users SET display_name = ? WHERE uuid = ?;";
		statement = connection.prepareStatement(query);
		statement.setString(1, username);
		statement.setString(2, this.uuid);
		statement.execute();
		statement.close();
	}

	public void updateEmail(String email) throws SQLException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "UPDATE users SET email = ? WHERE uuid = ?;";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, email);
		statement.setString(2, this.uuid);
		statement.execute();
		statement.close();

	}

	public static User getCurrent() {
		String uuid;
		User user;

		Subject currentSubject = SecurityUtils.getSubject();

		if (currentSubject.isAuthenticated()) {
			uuid = currentSubject.getPrincipal().toString();
			user = new User(uuid);
			try {
				Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
				String query = "select display_name, email from users where uuid = ?;";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, uuid);
				ResultSet resultSet = statement.executeQuery();
				resultSet.next();
				user.displayName = resultSet.getString(1);
				user.email = resultSet.getString(2);
				resultSet.close();
				statement.close();
			} catch (SQLException ignored) {}
		} else {
			user = new User();
		}

		return  user;
	}

	public boolean hasRole(Roles role) {
		return SecurityUtils.getSubject().hasRole(role.label);
	}

	public boolean isAdmin() {
		return hasRole(Roles.ADMIN);
	}
}
