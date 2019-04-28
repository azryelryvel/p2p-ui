package net.azry.p2p.ui.views.app;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.components.SidebarRouterLayout;

@Route(value = "app/dashboard", layout = SidebarRouterLayout.class)
public class DashboardView extends VerticalLayout {
	/*
		Bandwidth usage upload/download
		Volume of data sent/received
		Server bandwidth
	 */
}
