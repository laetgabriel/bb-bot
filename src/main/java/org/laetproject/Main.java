package org.laetproject;

import org.laetproject.dao.impl.ExperienciaDAOImpl;
import org.laetproject.db.DB;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {


        try{
           final ExperienciaDAOImpl experienciaDAO = new ExperienciaDAOImpl(DB.getConnection());

           experienciaDAO.criarTabela();
            Bot bot = new Bot();
        } catch (LoginException | IOException e) {
            System.out.println(e.getMessage());
        }

    }
}