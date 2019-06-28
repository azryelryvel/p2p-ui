package net.azry.p2pd.ui.components;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.component.AppBarNotificationButton;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import net.azry.p2pd.core.Config;
import net.azry.p2pd.ui.User;
import net.azry.p2pd.ui.views.app.*;
import net.azry.p2pd.ui.views.app.admin.HealthView;
import net.azry.p2pd.ui.views.app.admin.UsersManagementView;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
public class SidebarRouterLayout extends AppLayoutRouterLayout {
	private DefaultNotificationHolder notifications;

	private DefaultBadgeHolder badge;

	public SidebarRouterLayout() {
		String title = Config.properties.get("application.title");

		User currentUser = User.getCurrent();

		notifications = new DefaultNotificationHolder(newStatus -> {});
		badge = new DefaultBadgeHolder(5);
		LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), HomeView.class);
		badge.bind(menuEntry.getBadge());

		AppBarBuilder topMenuBuilder = AppBarBuilder.get();
		topMenuBuilder.add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications));

		LeftAppMenuBuilder leftMenuBuilder = LeftAppMenuBuilder.get();
		if (currentUser.hasPermission("view:home")) {
			leftMenuBuilder.addToSection(new LeftNavigationItem("Home", VaadinIcon.HOME.create(), HomeView.class), HEADER);
		}
		if (currentUser.hasPermission("view:dashboard")) {
			leftMenuBuilder.add(new LeftNavigationItem("Dashboard", VaadinIcon.DASHBOARD.create(), DashboardView.class));
		}
		if (currentUser.hasPermission("view:downloads")) {
			leftMenuBuilder.add(new LeftNavigationItem("Downloads", VaadinIcon.DOWNLOAD.create(), DownloadsView.class));
		}
		if (currentUser.hasPermission("view:uploads")) {
			leftMenuBuilder.add(new LeftNavigationItem("Uploads", VaadinIcon.UPLOAD.create(), HomeView.class));
		}
		if (currentUser.hasPermission("view:administration")) {
			LeftSubMenuBuilder adminSectionBuilder = LeftSubMenuBuilder.get("Administration", VaadinIcon.PLUS.create());
			if (currentUser.hasPermission("users:list")) {
				adminSectionBuilder.add(new LeftNavigationItem("Users", VaadinIcon.USERS.create(), UsersManagementView.class));
			}
			adminSectionBuilder.add(new LeftNavigationItem("Health", VaadinIcon.HEART.create(), HealthView.class));
			leftMenuBuilder.add(adminSectionBuilder.build());
		}
		if (currentUser.hasPermission("view:settings")) {
			leftMenuBuilder.addToSection(new LeftNavigationItem("Settings", VaadinIcon.COG.create(), SettingsView.class), FOOTER);
		}
		leftMenuBuilder.addToSection(new LeftClickableItem("Logout", VaadinIcon.SIGN_OUT.create(), clickEvent -> User.logout()), FOOTER);


		init(AppLayoutBuilder
				.get(Behaviour.LEFT_RESPONSIVE_HYBRID)
				.withTitle(title)
				.withAppBar(topMenuBuilder.build())
				.withAppMenu(leftMenuBuilder.build())
				.build()
		);
	}
}

