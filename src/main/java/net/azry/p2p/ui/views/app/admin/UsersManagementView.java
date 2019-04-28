package net.azry.p2p.ui.views.app.admin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.User;
import net.azry.p2p.ui.components.SidebarRouterLayout;
import net.azry.p2p.ui.shiro.authz.Roles;

import java.util.List;
import java.util.Set;

@Route(value = "app/admin/users", layout = SidebarRouterLayout.class)
public class UsersManagementView extends VerticalLayout {
	public UsersManagementView() {
		add(new H1("Administrators"));
		Set<User> adminList = User.listUsersByRole(Roles.ADMIN);
		Grid<User> adminGrid = new Grid<>(User.class);
		adminGrid.setItems(adminList);
		adminGrid.setColumns("displayName", "email");
		add(adminGrid);

		add(new H1("Users"));
		Set<User> userList = User.listUsersByRole(Roles.USER);
		Grid<User> userGrid = new Grid<>(User.class);
		userGrid.setItems(userList);
		userGrid.setColumns("displayName", "email");
		add(userGrid);
	}
}
