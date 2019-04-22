package net.azry.p2p.ui.authentication;

import org.apache.shiro.web.env.EnvironmentLoaderListener;

import javax.servlet.annotation.WebListener;

@WebListener
public class ShiroListener extends EnvironmentLoaderListener { }

