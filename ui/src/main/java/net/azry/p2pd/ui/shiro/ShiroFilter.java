package net.azry.p2pd.ui.shiro;

import org.vaadin.shiro.VaadinShiroFilter;

import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class ShiroFilter extends VaadinShiroFilter { }
