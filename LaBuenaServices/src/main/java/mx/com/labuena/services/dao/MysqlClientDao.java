package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;
import com.mysql.jdbc.Statement;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.models.Client;
import mx.com.labuena.services.models.Coordinates;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class MysqlClientDao extends BaseDao implements ClientDao {
    private static final Logger log = Logger.getLogger(MysqlClientDao.class.getName());

    @Inject
    public MysqlClientDao(Connection connection) {
        super(connection);
    }

    @Override
    public List<Client> getAll() throws InternalServerErrorException {
        List<Client> clients = new ArrayList<>();

        try {
            String clientsQuery = "select client.email, client.name, " +
                    "location.latitude, location.longitude from la_buena_db.client client " +
                    "join la_buena_db.location location" +
                    " on client.id_location = location.id_location;";
            ResultSet resultSet = connection.prepareStatement(clientsQuery).executeQuery();

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                BigDecimal latitude = resultSet.getBigDecimal("latitude");
                BigDecimal longitude = resultSet.getBigDecimal("longitude");

                clients.add(new Client(email, name, new Coordinates(latitude, longitude)));
            }
            resultSet.close();
            closeConnection(connection);
            return clients;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void save(Client client) throws InternalServerErrorException {
        try {
            try {
                Coordinates coordinates = client.getCoordinates();
                String saveLocationQuery = "insert into la_buena_db.location (id_location, latitude, longitude, created_at) values (0, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveLocationQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setBigDecimal(1, coordinates.getLatitude());
                preparedStatement.setBigDecimal(2, coordinates.getLongitude());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Registering client location failed, no rows affected.");
                }

                int locationId = 0;

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    locationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating location row failed, no ID obtained.");
                }

                String saveClientLocationQuery = "insert into la_buena_db.client (id_client, id_location, email, name) values (0, ?, ?, ?);";
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(saveClientLocationQuery);
                preparedStatement.setInt(1, locationId);
                preparedStatement.setString(2, client.getEmail());
                preparedStatement.setString(3, client.getName());
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

    @Override
    public Client findByEmail(String email) throws InternalServerErrorException {
        return null;
    }
}
