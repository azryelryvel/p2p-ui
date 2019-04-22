package com.example.ui;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;

import javax.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = "/*", name = "UiServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = UiUI.class, productionMode = false)
public class UiServlet extends VaadinServlet { }