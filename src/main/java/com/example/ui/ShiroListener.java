package com.example.ui;

import org.apache.shiro.web.env.EnvironmentLoaderListener;

import javax.servlet.annotation.WebListener;

@WebListener
public class ShiroListener extends EnvironmentLoaderListener { }

