package net.azry.p2pd.ui.views.app;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import net.azry.p2pd.ui.components.SidebarRouterLayout;
import net.azry.p2pd.ui.views.LoginView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * The main view of the application
 */
@Route(value = "app/home", layout = SidebarRouterLayout.class)
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

	public HomeView() {
		setClassName("app-view");

		Label hello = new Label("Hello Gradle app!");
		add(hello);

		Button button = new Button("Click me", event -> {
			SecurityUtils.getSubject().logout();
			if (getUI().isPresent()) {
				UI.getCurrent().getPage().reload();
			}
		});
		add(button);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Subject currentUser = SecurityUtils.getSubject();
		if (! currentUser.isAuthenticated()) {
			event.forwardTo(LoginView.class);
		}
	}
}