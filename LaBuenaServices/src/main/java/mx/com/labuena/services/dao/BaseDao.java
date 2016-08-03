package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BaseDao {
    private static final Logger log = Logger.getLogger(BaseDao.class.getName());

    private DataSource dataSource;

    public BaseDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection openConnection() throws InternalServerErrorException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void closeConnection(Connection connection) throws InternalServerErrorException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
                throw new InternalServerErrorException(e);
            }
        }
    }
}
