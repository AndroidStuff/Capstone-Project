package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BaseDao {
    private static final Logger log = Logger.getLogger(BaseDao.class.getName());

    protected Connection openConnection() throws InternalServerErrorException {
        Connection conn = null;

        try {
            String url;
            if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
                url = System.getProperty("ae-cloudsql.cloudsql-database-url");
                try {
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                } catch (ClassNotFoundException exception) {
                    throw new InternalServerErrorException("Error loading Google JDBC Driver", exception);
                }
            } else {
                url = System.getProperty("ae-cloudsql.local-database-url");
            }

            conn = DriverManager.getConnection(url);

            return conn;
        } catch (InternalServerErrorException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (SQLException e) {
            closeConnection(conn);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    protected void closeConnection(Connection conn) throws InternalServerErrorException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
                throw new InternalServerErrorException(e);
            }
        }
    }
}
