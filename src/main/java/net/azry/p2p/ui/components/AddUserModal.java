package net.azry.p2p.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import net.azry.p2p.ui.User;
import net.azry.p2p.ui.shiro.authc.AuthenticationUtils;
import net.azry.p2p.ui.shiro.authz.Roles;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AddUserModal extends Dialog {
	public AddUserModal(Grid<User> usersGrid) {
		VerticalLayout addUserLayout = new VerticalLayout();

		H1 title = new H1("Register");

		TextField displayNameTextField = new TextField();
		displayNameTextField.setPlaceholder("john_doe_321");
		displayNameTextField.setTitle("Username");
		displayNameTextField.setLabel("Username");
		displayNameTextField.setSizeFull();
		displayNameTextField.setRequired(true);

		HorizontalLayout emailLayout = new HorizontalLayout();

		TextField emailTextField = new TextField();
		emailTextField.setPlaceholder("john@doe.net");
		emailTextField.setTitle("E-Mail");
		emailTextField.setLabel("E-Mail");
		emailTextField.setClearButtonVisible(true);
		emailTextField.setSizeFull();
		emailTextField.setPreventInvalidInput(true);
		emailTextField.setRequired(true);

		Checkbox notifyCheckbox = new Checkbox();
		notifyCheckbox.setLabel("Notify");
		emailLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, notifyCheckbox);

		emailLayout.add(emailTextField, notifyCheckbox);


		HorizontalLayout passwordLayout = new HorizontalLayout();
		TextField passwordField = new TextField();
		passwordField.setRequired(true);
		passwordField.setPlaceholder("your-secret-pass321");
		passwordField.setTitle("Password");
		passwordField.setLabel("Password");

		Button generatePasswordButton = new Button("Rand", event -> {
			String password = AuthenticationUtils.generateRandomPlainPassword(12);
			passwordField.setValue(password);
		});
		passwordLayout.add(passwordField, generatePasswordButton);
		passwordLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, generatePasswordButton);


		Button registerButton = new Button("Register", event -> {
			if (usersGrid != null) {
				User user = new User();
				user.setDisplayName(displayNameTextField.getValue());
				user.setEmail(emailTextField.getValue());
				try {
					user.register(passwordField.getValue());
					user.addRole(Roles.USER);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Set<User> currentSet = usersGrid.getDataProvider().fetch(new Query<>()).collect(Collectors.toUnmodifiableSet());
				Set<User> updatedSet = new HashSet<>(currentSet);
				updatedSet.add(user);
				usersGrid.setItems(updatedSet);
			}
			this.close();
		});
		registerButton.setSizeFull();

		addUserLayout.add(title, displayNameTextField, emailLayout, passwordLayout, registerButton);
		add(addUserLayout);
	}
}
