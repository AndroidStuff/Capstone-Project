package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.models.Order;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class MysqlOrderDao  extends BaseDao implements OrderDao {
    private static final Logger log = Logger.getLogger(MysqlOrderDao.class.getName());

    @Inject
    public MysqlOrderDao(Connection connection) {
        super(connection);
    }

    @Override
    public int save(Order order) throws InternalServerErrorException {
        try {
            try {

                String saveBranchQuery = "insert into la_buena_db.order " +
                        "(id_order, id_client, id_biker, quantity) values (0, ?, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveBranchQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1,order.getClientId());
                preparedStatement.setInt(2, order.getBikerId());
                preparedStatement.setInt(3, order.getQuantity());
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Registering order failed, no rows affected.");
                }

                int orderId = 0;

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order row failed, no ID obtained.");
                }

                connection.commit();

                return orderId;

            } catch (SQLException e) {
                connection.rollback();
                log.log(Level.SEVERE, e.getMessage(), e);
                throw new InternalServerErrorException(e);
            } finally {
                connection.setAutoCommit(true);
                closeConnection(connection);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
