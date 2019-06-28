package net.azry.p2pd.ui;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;

import javax.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = "/*", name = "MainServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
public class MainServlet extends VaadinServlet { }