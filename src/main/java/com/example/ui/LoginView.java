package com.example.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;

/**
 * The main view of the application
 */
@Route("login")
@BodySize(height = "100vh", width = "200vw")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class LoginView extends VerticalLayout {

    public LoginView() {
        LoginForm component = new LoginForm();
        component.addLoginListener(e -> {
            boolean isAuthenticated = true;
            Shiro shiro = new Shiro();
            shiro.setUsername("test");
            shiro.setPassword("testp");
            System.out.println("testy");
            if (shiro.loginUser()) {
                System.out.println("OK");
            } else {
                component.setError(true);
            }
        });
        add(component);
    }
}