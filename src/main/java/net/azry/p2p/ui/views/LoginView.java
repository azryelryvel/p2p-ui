package net.azry.p2p.ui.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import net.azry.p2p.ui.authentication.User;

import java.sql.SQLException;

/**
 * The main view of the application
 */
@Route("login")
@BodySize(height = "100vh", width = "100vw")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class LoginView extends VerticalLayout {
    public LoginView() {
        LoginForm component = new LoginForm();
        component.addLoginListener(e -> {
            User user = new User(e.getUsername());

            if (user.login(e.getPassword())) {
                System.out.println("Login OK");
            } else {
                try {
                    user.register(e.getPassword());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    component.setError(true);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    component.setError(true);
                }
            }
        });
        add(component);
    }
}