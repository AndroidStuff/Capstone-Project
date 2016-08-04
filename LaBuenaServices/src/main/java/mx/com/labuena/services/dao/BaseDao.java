package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BaseDao {
    private static final Logger log = Logger.getLogger(BaseDao.class.getName());
    protected final ConnectionProvider connectionProvider;

    public BaseDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
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
