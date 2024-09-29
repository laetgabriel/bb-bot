package org.laetproject.db;

import org.laetproject.db.exceptions.DBException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
            }
        }
        return connection;
    }

    private static Properties getProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }
}
