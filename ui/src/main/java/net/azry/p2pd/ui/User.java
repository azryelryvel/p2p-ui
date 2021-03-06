package net.azry.p2pd.ui;

import com.vaadin.flow.component.UI;
import net.azry.p2pd.ui.shiro.authc.AuthenticationUtils;
import net.azry.p2pd.ui.exceptions.UsernameAlreadyExistsException;
import net.azry.p2pd.ui.database.DatabaseConnectionFactory;
import net.azry.p2pd.ui.shiro.authz.Roles;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.*;

public class User {
	private String uuid;

	private String displayName;

	private String email;

	private boolean locked;

	private Set<Roles> roles = new HashSet<>();

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
		String query = "INSERT INTO users (uuid, display_name, email, password, salt) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.setString(2, displayName);
		statement.setString(3, email);
		statement.setString(4, creds[0]);
		statement.setString(5, creds[1]);

		statement.execute();
		statement.close();
	}

	public void delete() throws SQLException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "DELETE FROM users WHERE uuid = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.execute();
		statement.close();
	}

	public void lock() throws SQLException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "UPDATE users SET locked = 1 WHERE uuid = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.execute();
		statement.close();
	}

	public void unlock() throws SQLException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "UPDATE users SET locked = 0 WHERE uuid = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.execute();
		statement.close();
	}

	public void addRole(Roles role) throws SQLException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();

		String query = "SELECT 1 FROM roles WHERE role = ? AND uuid = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, role.toString());
		statement.setString(2, uuid);
		ResultSet rs = statement.executeQuery();
		boolean alreadyHasRole = rs.next();
		rs.close();
		statement.close();
		if (alreadyHasRole) {
			return;
		}

		query = "INSERT INTO roles (uuid, role) values (?, ?);";
		statement = connection.prepareStatement(query);
		statement.setString(1, uuid);
		statement.setString(2, role.toString());
		statement.execute();
		statement.close();

		roles.add(role);
	}

	public void updatePassword(String password) throws SQLException {
		String[] newCreds = AuthenticationUtils.generateSaltedHashedPassword(password);
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "UPDATE users SET password = ?, salt = ? WHERE uuid = ?;";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, newCreds[0]);
		statement.setString(2, newCreds[1]);
		statement.setString(3, this.uuid);
		statement.execute();
		statement.close();
	}

	public void updateDisplayName(String displayName) throws SQLException, UsernameAlreadyExistsException {
		Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
		String query = "SELECT 1 FROM users WHERE display_name = ?;";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, displayName);
		ResultSet rs = statement.executeQuery();
		boolean userExists = rs.next();
		rs.close();
		statement.close();
		if (userExists) {
			throw new UsernameAlreadyExistsException("Username " + displayName + " already exists");
		}

		query = "UPDATE users SET display_name = ? WHERE uuid = ?;";
		statement = connection.prepareStatement(query);
		statement.setString(1, displayName);
		statement.setString(2, this.uuid);
		statement.execute();
		statement.close();
		this.displayName = displayName;
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
		return SecurityUtils.getSubject().hasRole(role.toString());
	}

	public boolean hasPermission(String permission) {
		return SecurityUtils.getSubject().isPermitted(permission);
	}

	public static Set<User> listUsersByRole(Roles role) {
		Set<User> userList = new HashSet<>();
		try {
			Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
			String query = "SELECT users.uuid, users.display_name, users.email, users.locked FROM users LEFT JOIN (roles) ON (roles.uuid = users.uuid) WHERE roles.role = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, role.toString());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String uuid = rs.getString(1);
				String displayName = rs.getString(2);
				String email = rs.getString(3);
				boolean locked = rs.getBoolean(4);

				User user = new User(uuid);
				user.displayName = displayName;
				user.email = email;
				user.locked = locked;
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public static Set<User> listAllUsers() {
		Set<User> userList = new HashSet<>();
		try {
			Connection connection = DatabaseConnectionFactory.getDatabaseConnection();
			String query = "SELECT users.uuid, users.display_name, users.email, users.locked, roles.role FROM users LEFT JOIN (roles) ON (roles.uuid = users.uuid);";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String uuid = rs.getString(1);
				String displayName = rs.getString(2);
				String email = rs.getString(3);
				boolean locked = rs.getBoolean(4);
				String roleString = rs.getString(5);
				Optional<Roles> role = Arrays.stream(Roles.values()).filter(r -> r.toString().equals(roleString)).findAny();

				Optional<User> existingUser = userList.stream().filter(u -> u.getUuid().equals(uuid)).findAny();

				if (existingUser.isPresent()) {
					role.ifPresent(r -> existingUser.get().roles.add(r));
				} else {
					User user = new User(uuid);
					user.displayName = displayName;
					user.email = email;
					user.locked = locked;
					user.roles = new HashSet<>();
					role.ifPresent(r -> user.roles.add(r));
					userList.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public String getUuid() {
		return uuid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public boolean isLocked() {
		return locked;
	}

	@Override
	public boolean equals(Object otherUser) {
		if (this == otherUser) {
			return true;
		}
		if (otherUser == null || getClass() != otherUser.getClass()) {
			return false;
		}
		return ((User) otherUser).getUuid().equals(getUuid());

	}
}
