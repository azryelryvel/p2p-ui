package net.azry.p2p.ui.views.app.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.User;
import net.azry.p2p.ui.components.AddUserModal;
import net.azry.p2p.ui.components.SidebarRouterLayout;
import org.apache.shiro.SecurityUtils;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "app/admin/users", layout = SidebarRouterLayout.class)
public class UsersManagementView extends VerticalLayout {
	public UsersManagementView() {
		if (SecurityUtils.getSubject().isPermitted("users:list")) {
			Grid<User> usersGrid = new Grid<>(User.class);
			Set<User> usersSet = User.listAllUsers();
			usersGrid.setItems(usersSet);
			usersGrid.setColumns("displayName", "email", "roles");
			usersGrid.getColumnByKey("displayName").setHeader("Username");
			usersGrid.getColumnByKey("email").setHeader("E-Mail");
			usersGrid.addColumn(new ComponentRenderer<>(user -> {
				if (user.isLocked()) {
					Icon lockedIcon = new Icon(VaadinIcon.LOCK);
					lockedIcon.addClickListener(event -> {
						try {
							user.unlock();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					});
					return lockedIcon;
				} else {
					Icon unlockedIcon = new Icon(VaadinIcon.UNLOCK);
					unlockedIcon.addClickListener(event -> {
						try {
							user.lock();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					});
					return unlockedIcon;
				}
			})).setHeader("Locked");
			usersGrid.addColumn(new NativeButtonRenderer<>("Delete", user -> {
				try {
					user.delete();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				Set<User> currentSet = usersGrid.getDataProvider().fetch(new Query<>()).collect(Collectors.toUnmodifiableSet());
				Set<User> newUserSet = currentSet.stream().filter(u -> !user.equals(u)).collect(Collectors.toUnmodifiableSet());
				usersGrid.setItems(newUserSet);
			}));

			add(new H1("Users"));

			if (SecurityUtils.getSubject().isPermitted("users:add")) {
				AddUserModal addUserDialog = new AddUserModal(usersGrid);
				Button addUserButton = new Button("Add user", event -> {
					addUserDialog.open();
				});
				add(addUserButton);
			}

			add(usersGrid);
		}
	}
}
