package net.azry.p2p.ui.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.authentication.AuthenticationUtils;
import net.azry.p2p.ui.authentication.User;

/**
 * The main view of the application
 */
@Route("login")
@BodySize(height = "100vh", width = "100vw")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	public LoginView() {
		AuthenticationUtils.initialize();
		setSizeFull();

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		horizontalLayout.setHeight("70%");

		LoginForm component = new LoginForm();
		component.addLoginListener(e -> {
			User user = new User(e.getUsername());

			if (user.login(e.getPassword())) {
				component.getUI().ifPresent(ui -> ui.navigate(HomeView.class));
			} else {
				component.setError(true);
			}
		});
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		horizontalLayout.add(component);
		add(horizontalLayout);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {


	}
}