package net.azry.p2pd.ui.views.app.admin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.azry.p2pd.ui.grpc.BackendService;
import net.azry.p2pd.ui.components.SidebarRouterLayout;

@Route(value = "app/admin/health", layout = SidebarRouterLayout.class)
public class HealthView extends VerticalLayout {
	public HealthView() {
		add(new H1("Health"));
		boolean alive = new BackendService().check();
		add(new H2("Backend: " + (alive ? "Up" : "Down")));
	}
}
