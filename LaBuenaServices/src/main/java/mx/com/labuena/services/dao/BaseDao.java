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
    private final DataSource dataSource;

    public BaseDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection openConnection() throws InternalServerErrorException {
        Connection conn = null;
        try {
            return dataSource.getConnection();
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
