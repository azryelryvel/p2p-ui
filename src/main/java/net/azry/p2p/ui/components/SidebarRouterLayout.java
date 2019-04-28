package net.azry.p2p.ui.components;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.component.AppBarNotificationButton;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.User;
import net.azry.p2p.ui.views.app.DashboardView;
import net.azry.p2p.ui.views.app.HomeView;
import net.azry.p2p.ui.views.app.SettingsView;
import net.azry.p2p.ui.views.app.admin.UsersManagementView;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
public class SidebarRouterLayout extends AppLayoutRouterLayout {
	private DefaultNotificationHolder notifications;

	private DefaultBadgeHolder badge;

	public SidebarRouterLayout() {
		String title = Config.properties.get("application.title");

		boolean isAdmin = User.getCurrent().isAdmin();
		LeftNavigationItem adminNavigationItem = new LeftNavigationItem("Administration", VaadinIcon.COGS.create(), UsersManagementView.class);

		notifications = new DefaultNotificationHolder(newStatus -> {});
		badge = new DefaultBadgeHolder(5);
		LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), HomeView.class);
		badge.bind(menuEntry.getBadge());
		init(AppLayoutBuilder
				.get(Behaviour.LEFT_RESPONSIVE_HYBRID)
				.withTitle(title)
				.withAppBar(AppBarBuilder.get()
						.add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications))
						.build()
				)
				.withAppMenu(LeftAppMenuBuilder.get()
						.addToSection(new LeftNavigationItem("Home", VaadinIcon.HOME.create(), HomeView.class), HEADER)
						.add(new LeftNavigationItem("My downloads", VaadinIcon.DOWNLOAD.create(), HomeView.class))
						.add(new LeftNavigationItem("My uploads", VaadinIcon.UPLOAD.create(), HomeView.class))
						.add(new LeftNavigationItem("Shared downloads", VaadinIcon.FOLDER.create(), HomeView.class))
						.add(new LeftNavigationItem("Dashboard", VaadinIcon.DASHBOARD.create(), DashboardView.class))
						.addToSection(new LeftNavigationItem("Settings", VaadinIcon.COG.create(), SettingsView.class), FOOTER)
						.addToSection(new LeftClickableItem("Logout", VaadinIcon.SIGN_OUT.create(), clickEvent -> User.logout()), FOOTER)
						.build()
				)
				.build()
		);
	}
}

