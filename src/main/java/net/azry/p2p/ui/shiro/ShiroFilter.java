package net.azry.p2p.ui.shiro;

import org.vaadin.shiro.VaadinShiroFilter;

import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class ShiroFilter extends VaadinShiroFilter { }
