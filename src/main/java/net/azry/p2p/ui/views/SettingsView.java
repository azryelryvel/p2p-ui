package net.azry.p2p.ui.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.authentication.User;
import net.azry.p2p.ui.components.SidebarRouterLayout;

@Route(value = "settings", layout = SidebarRouterLayout.class)
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

		EmailField emailField = new EmailField();
		emailField.setLabel("E-Mail");
		emailField.setPlaceholder("john.doe.321@example.com");
		emailField.setWidth(width);
		emailField.setMinWidth(minWidth);

		PasswordField passwordField = new PasswordField();
		passwordField.setLabel("Password");
		passwordField.setWidth(width);
		passwordField.setMinWidth(minWidth);

		Button saveButton = new Button("Save");

		add(usernameField, emailField, passwordField, saveButton);

	}
}
