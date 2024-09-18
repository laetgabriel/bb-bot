package org.laetproject;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {

        try{
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }
    }
}