package org.laetproject;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {

        System.out.println(System.getenv("TOKEN"));
        try{
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }
    }
}