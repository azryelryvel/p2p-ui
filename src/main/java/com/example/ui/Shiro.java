package com.example.ui;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;

public class Shiro {

    private String username, password;
    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean loginUser(){
        boolean isAuthenticated = true;
        System.out.println("test1");
        try{

            Subject currentUser         = SecurityUtils.getSubject();
            System.out.println("test2");
            UsernamePasswordToken token = new UsernamePasswordToken(username, new Sha256Hash(password).toHex());
            System.out.println("test3");

            token.setRememberMe(rememberMe);
            System.out.println("test4");
            currentUser.login(token);
            System.out.println("test5");

            isAuthenticated = currentUser.isAuthenticated();

        } catch (IllegalStateException e) {
            System.out.println("illegal");
        } catch (UnknownAccountException uae ) {
            System.out.println("test");
        } catch (IncorrectCredentialsException ice ) {
            System.out.println("test");
        } catch (LockedAccountException lae ) {
            System.out.println("test");
        } catch(AuthenticationException aex){
            System.out.println("test");
        }

        return isAuthenticated;

    }

    public void authorizedUserControl(){

        if(null != SecurityUtils.getSubject().getPrincipal()){
        }
    }
}
