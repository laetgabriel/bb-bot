package org.laetproject;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {

        try{
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }
    }
}