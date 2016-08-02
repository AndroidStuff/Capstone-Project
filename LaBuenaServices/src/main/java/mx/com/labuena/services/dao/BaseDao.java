package mx.com.labuena.services.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BaseDao {
    protected Connection openConnection() {
        Connection conn = null;

        try {
            String url;
            if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
                url = System.getProperty("ae-cloudsql.cloudsql-database-url");
                try {
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                } catch (ClassNotFoundException exception) {
                    throw new ServletException("Error loading Google JDBC Driver", exception);
                }
            } else {
                url = System.getProperty("ae-cloudsql.local-database-url");
            }

            conn = DriverManager.getConnection(url);

            return conn;
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            closeConnection(conn);
        }
        return null;
    }

    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
