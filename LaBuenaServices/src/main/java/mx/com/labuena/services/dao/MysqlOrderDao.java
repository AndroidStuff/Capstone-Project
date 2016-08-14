package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
                preparedStatement.setDouble(1, coordinates.getLatitude());
                preparedStatement.setDouble(2, coordinates.getLongitude());

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

    @Override
    public List<Order> findByBikerId(int bikerId) throws InternalServerErrorException {
        List<Order> orders = new ArrayList<>();
        Connection connection = connectionProvider.get();
        try {
            String ordersQuery = "select order.id_order, client.email, client.name, coordinates.latitude, " +
                    "coordinates.longitude, order.quantity from la_buena_db.order " +
                    "join la_buena_db.client on order.id_client = client.id_client " +
                    "join la_buena_db.location coordinates on order.id_location = coordinates.id_location " +
                    "join la_buena_db.biker on order.id_biker = biker.id_biker and biker.id_biker = ? " +
                    "where order.delivered = 0;";
            PreparedStatement preparedStatement = connection.prepareStatement(ordersQuery);
            preparedStatement.setInt(1, bikerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id_order");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                int quantity = resultSet.getInt("quantity");

                orders.add(new Order(orderId, email, name, new Coordinates(latitude, longitude), quantity));
            }
            resultSet.close();
            closeConnection(connection);
            return orders;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void updateOrder(Order order) throws InternalServerErrorException {
        try {
            Connection connection = connectionProvider.get();
            try {
                String saveBikerQuery = "update la_buena_db.order set delivered = 1 " +
                        "where id_order = ?;";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveBikerQuery);
                preparedStatement.setInt(1, order.getOrderId());
                preparedStatement.execute();
                connection.commit();
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
