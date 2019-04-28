package net.azry.p2p.ui.views.app;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.User;
import net.azry.p2p.ui.components.SidebarRouterLayout;

import java.sql.SQLException;

@Route(value = "app/settings", layout = SidebarRouterLayout.class)
public class SettingsView extends VerticalLayout {
	public SettingsView() {

		User user = User.getCurrent();

		add(new H1("Settings"));

		String minWidth = "300px";
		String width = "30%";
		TextField usernameField = new TextField();
		usernameField.setLabel("Username");
		usernameField.setPlaceholder("john_doe_321");
		usernameField.setWidth(width);
		usernameField.setMinWidth(minWidth);
		usernameField.setValue(user.getDisplayName());

		EmailField emailField = new EmailField();
		emailField.setLabel("E-Mail");
		emailField.setPlaceholder("john.doe.321@example.com");
		emailField.setWidth(width);
		emailField.setMinWidth(minWidth);
		emailField.setValue(user.getEmail());

		PasswordField passwordField = new PasswordField();
		passwordField.setLabel("Password");
		passwordField.setWidth(width);
		passwordField.setMinWidth(minWidth);
		passwordField.setPlaceholder("Set new password");

		Button saveButton = new Button("Save", event -> {
			String newUsernname = usernameField.getValue();
			String newEmail = emailField.getValue();
			String newPassword = passwordField.getValue();

			if (!user.getDisplayName().equals(newUsernname)) {
				try {
					user.updateDisplayName(newUsernname);
				} catch (SQLException e) {
					// TODO:  add a div showing that there was a problem
					e.printStackTrace();
				}
			}
			if (!user.getEmail().equals(newEmail)) {
				try {
					user.updateEmail(newEmail);
				} catch (SQLException e) {
					// TODO:  add a div showing that there was a problem
					e.printStackTrace();
				}
			}


			if (!passwordField.isEmpty()) {
				try {
					user.updatePassword(newPassword);
				} catch (SQLException e) {
					// TODO:  add a div showing that there was a problem
					e.printStackTrace();
				}
			}
		});

		add(usernameField, emailField, passwordField, saveButton);

	}
}
