package net.azry.p2p.ui.components;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftBadgeIconItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.component.AppBarNotificationButton;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Push;
import net.azry.p2p.ui.Config;
import net.azry.p2p.ui.views.DashboardView;
import net.azry.p2p.ui.views.HomeView;
import net.azry.p2p.ui.views.SettingsView;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
public class SidebarRouterLayout extends AppLayoutRouterLayout {
	private DefaultNotificationHolder notifications;

	private DefaultBadgeHolder badge;

	public SidebarRouterLayout() {
		String title = Config.properties.get("application.title");

		notifications = new DefaultNotificationHolder(newStatus -> {});
		badge = new DefaultBadgeHolder(5);
		/*
		for (int i = 1; i < 6; i++) {
			notifications.addNotification(new DefaultNotification("Test title" + i, "A rather long test description ..............." + i));
		}
		*/
		LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), HomeView.class);
		badge.bind(menuEntry.getBadge());
		init(AppLayoutBuilder
				.get(Behaviour.LEFT_RESPONSIVE_HYBRID)
				.withTitle(title)
				.withAppBar(AppBarBuilder.get()
						.add(new AppBarNotificationButton<>(VaadinIcon.BELL, notifications))
						.build())
				.withAppMenu(LeftAppMenuBuilder.get()
						.add(new LeftNavigationItem("Home", VaadinIcon.HOME.create(), HomeView.class))
						.add(new LeftNavigationItem("My downloads", VaadinIcon.DOWNLOAD.create(), HomeView.class))
						.add(new LeftNavigationItem("My uploads", VaadinIcon.UPLOAD.create(), HomeView.class))
						.add(new LeftNavigationItem("Shared downloads", VaadinIcon.FOLDER.create(), HomeView.class))
						.add(new LeftNavigationItem("Dashboard", VaadinIcon.DASHBOARD.create(), DashboardView.class))
						.add(new LeftNavigationItem("Settings", VaadinIcon.COG.create(), SettingsView.class))
						.build())
				.build());

/*
						.add(LeftSubMenuBuilder.get("My Submenu", VaadinIcon.PLUS.create())
								.add(LeftSubMenuBuilder.get("My Submenu", VaadinIcon.PLUS.create())
										.add(new LeftNavigationItem("Charts", VaadinIcon.SPLINE_CHART.create(), HomeView.class))
										.add(new LeftNavigationItem("Contact", VaadinIcon.CONNECT.create(), HomeView.class))
										.add(new LeftNavigationItem("More", VaadinIcon.COG.create(), HomeView.class))
										.build())
								.add(new LeftNavigationItem("Contact1", VaadinIcon.CONNECT.create(), HomeView.class))
								.add(new LeftNavigationItem("More1", VaadinIcon.COG.create(), HomeView.class))
								.build())
						.add(menuEntry)
*/
	}
}

