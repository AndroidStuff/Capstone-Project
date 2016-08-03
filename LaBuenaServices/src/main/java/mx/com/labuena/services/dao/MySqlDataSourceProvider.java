package mx.com.labuena.services.dao;

import com.google.inject.Provider;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class MySqlDataSourceProvider implements Provider<DataSource> {
    private static final Logger log = Logger.getLogger(MySqlDataSourceProvider.class.getName());

    @Override
    public DataSource get() {
        Properties props = new Properties();
        FileInputStream fis = null;
        MysqlDataSource mysqlDataSource = null;
        try {
            fis = new FileInputStream("WEB-INF/db.properties");
            props.load(fis);
            mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDataSource.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDataSource.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
            Class.forName("com.mysql.jdbc.GoogleDriver");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return mysqlDataSource;
    }
}
