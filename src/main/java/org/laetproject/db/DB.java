package org.laetproject.db;

import org.laetproject.db.exceptions.DBException;
import org.laetproject.util.FileUtils;

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
                Properties props = getProperties();
                String url = props.getProperty("dburl");
                connection = DriverManager.getConnection(url);

                System.out.println("Conectado ao Banco de Dados");
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return connection;
    }

    private static Properties getProperties() {
        try (FileInputStream fs = new FileInputStream("src/main/resources/db.properties")) {
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
