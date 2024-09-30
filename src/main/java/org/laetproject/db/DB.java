package org.laetproject.db;

import org.laetproject.db.exceptions.DBException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public final class DB {

    private static Connection connection = null;

    private DB() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {

                connection = DriverManager.getConnection("jdbc:sqlite:database.db");

                System.out.println("Conectado ao Banco de Dados");
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }

        return connection;
    }

    private static Properties getProperties() {
        try (FileInputStream fs = new FileInputStream("src/main/java/org/laetproject/db/db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }
}
