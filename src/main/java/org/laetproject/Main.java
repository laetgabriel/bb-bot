package org.laetproject;

import org.laetproject.dao.impl.CargoDAOImpl;
import org.laetproject.dao.impl.ExperienciaDAOImpl;
import org.laetproject.db.DB;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {


        try{
           final ExperienciaDAOImpl experienciaDAO = new ExperienciaDAOImpl(DB.getConnection());
           final CargoDAOImpl cargoDAO = new CargoDAOImpl(DB.getConnection());
           cargoDAO.criarTabela();
           experienciaDAO.criarTabela();
            Bot bot = new Bot();
        } catch (LoginException | IOException e) {
            System.out.println(e.getMessage());
        }

    }
}