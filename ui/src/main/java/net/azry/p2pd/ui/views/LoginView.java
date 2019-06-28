package net.azry.p2pd.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import net.azry.p2pd.ui.shiro.authc.AuthenticationUtils;
import net.azry.p2pd.ui.User;
import org.apache.shiro.authc.UnknownAccountException;

import java.sql.SQLException;

/**
 * The main view of the application
 */
@Route("login")
@BodySize(height = "100vh", width = "100vw")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class LoginView extends VerticalLayout {
	public LoginView() {
		AuthenticationUtils.initialize();
		setSizeFull();

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		horizontalLayout.setHeight("70%");

		LoginForm component = new LoginForm();
		component.addLoginListener(e -> {
			User user = null;
			try {
				user = User.getByDisplayName(e.getUsername());
			} catch (SQLException | UnknownAccountException ex) {
				ex.printStackTrace();
				component.setError(true);
				return;
			}

			if (user.login(e.getPassword())) {
				// Required to enable Push. UI.navigate does not enable it.
				UI.getCurrent().getPage().executeJavaScript("window.location.href = 'app/home'");
			} else {
				component.setError(true);
			}
		});
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		horizontalLayout.add(component);
		add(horizontalLayout);
	}
}