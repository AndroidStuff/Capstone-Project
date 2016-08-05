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

import mx.com.labuena.services.models.Coordinates;
import mx.com.labuena.services.models.Order;
import mx.com.labuena.services.models.OrderDao;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class MysqlOrderDao extends BaseDao implements OrderDao {
    private static final Logger log = Logger.getLogger(MysqlOrderDao.class.getName());

    @Inject
    public MysqlOrderDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public int save(Order order) throws InternalServerErrorException {
        Connection connection = connectionProvider.get();
        try {
            try {

                Coordinates coordinates = order.getCoordinates();
                String saveLocationQuery = "insert into la_buena_db.location (id_location, latitude, longitude) values (0, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveLocationQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setBigDecimal(1, coordinates.getLatitude());
                preparedStatement.setBigDecimal(2, coordinates.getLongitude());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Registering order location failed, no rows affected.");
                }

                int locationId = 0;

                ResultSet locationGeneratedKeys = preparedStatement.getGeneratedKeys();
                if (locationGeneratedKeys.next()) {
                    locationId = locationGeneratedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating location row failed, no ID obtained.");
                }

                String saveBranchQuery = "insert into la_buena_db.order " +
                        "(id_order, id_client, id_biker, quantity, id_location) values (0, ?, ?, ?, ?);";
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(saveBranchQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, order.getClientId());
                preparedStatement.setInt(2, order.getBikerId());
                preparedStatement.setInt(3, order.getQuantity());
                preparedStatement.setInt(4, locationId);
                affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Registering order failed, no rows affected.");
                }

                int orderId = 0;

                ResultSet orderGeneratedKeys = preparedStatement.getGeneratedKeys();
                if (orderGeneratedKeys.next()) {
                    orderId = orderGeneratedKeys.getInt(1);
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
