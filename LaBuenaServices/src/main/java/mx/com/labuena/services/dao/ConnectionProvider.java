package mx.com.labuena.services.dao;

import com.google.inject.Provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class ConnectionProvider implements Provider<Connection> {
    private static final Logger log = Logger.getLogger(ConnectionProvider.class.getName());

    private static final String URL_FORMAT ="%s?user=%s&amp;password=%s";
    @Override
    public Connection get() {

        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("WEB-INF/db.properties");

            props.load(fis);

            String server = props.getProperty("MYSQL_DB_URL");
            String user = props.getProperty("MYSQL_DB_USERNAME");
            String password = props.getProperty("MYSQL_DB_PASSWORD");

            String url = String.format(URL_FORMAT, server, user, password);

            Class.forName("com.mysql.jdbc.GoogleDriver");

            return DriverManager.getConnection(url);

        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
    }
}
