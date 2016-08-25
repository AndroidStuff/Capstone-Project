package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.models.BikeDriverSelector;
import mx.com.labuena.services.models.Biker;
import mx.com.labuena.services.models.BikerDao;
import mx.com.labuena.services.models.BikerLocation;
import mx.com.labuena.services.models.Coordinates;
import mx.com.labuena.services.utils.DateExtensor;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class MysqlBikerDao extends BaseDao implements BikerDao, BikeDriverSelector {
    private static final Logger log = Logger.getLogger(MysqlBikerDao.class.getName());

    @Inject
    public MysqlBikerDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public List<Biker> getAll() throws InternalServerErrorException {
        List<Biker> bikers = new ArrayList<>();
        Connection connection = connectionProvider.get();
        try {
            String bikersQuery = "select name, email, phone, stock, location.latitude, location.longitude, location.created_at \n" +
                    "from la_buena_db.biker \n" +
                    "left join (select biker.id_biker, max(biker_location.id_location) as id_location \n" +
                    "\tfrom la_buena_db.biker \n" +
                    "\tjoin la_buena_db.biker_location on biker.id_biker = biker_location.id_biker group by biker.id_biker) latest_location \n" +
                    "on biker.id_biker = latest_location.id_biker \n" +
                    "left join la_buena_db.location on latest_location.id_location = location.id_location;";
            ResultSet rs = connection.prepareStatement(bikersQuery).executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String name = rs.getString("name");
                int stock = rs.getInt("stock");
                Biker biker = new Biker(name, email, phone, stock);
                Date readAt = rs.getDate("created_at");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                BikerLocation bikerLocation = new BikerLocation(readAt,
                        new Coordinates(latitude, longitude));
                biker.setBikerLocation(bikerLocation);
                bikers.add(biker);
            }
            rs.close();
            closeConnection(connection);
            return bikers;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void save(Biker biker) throws InternalServerErrorException {
        try {
            Connection connection = connectionProvider.get();
            try {
                String branchesQuery = "select id_branch from la_buena_db.branch";
                ResultSet resultSet = connection.prepareStatement(branchesQuery).executeQuery();
                int branchId = 0;
                while (resultSet.next()) {
                    branchId = resultSet.getInt("id_branch");
                    break;
                }
                resultSet.close();

                String saveBikerQuery = "insert into la_buena_db.biker (id_biker, email, name, stock, phone, id_branch) values (0, ?, ?, 0, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveBikerQuery);
                preparedStatement.setString(1, biker.getEmail());
                preparedStatement.setString(2, biker.getName());
                preparedStatement.setString(3, biker.getPhone());
                preparedStatement.setInt(4, branchId);
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
    public void saveLocation(Biker biker) throws InternalServerErrorException {
        try {
            Connection connection = connectionProvider.get();
            try {
                BikerLocation bikerLocation = biker.getBikerLocation();
                Coordinates coordinates = bikerLocation.getCoordinates();
                String saveLocationQuery = "insert into la_buena_db.location (id_location, latitude, longitude, created_at) values (0, ?, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveLocationQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setDouble(1, coordinates.getLatitude());
                preparedStatement.setDouble(2, coordinates.getLongitude());
                preparedStatement.setDate(3, DateExtensor.toSqlDate(bikerLocation.getReadAt()));

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Registering last biker location failed, no rows affected.");
                }

                int locationId = 0;

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    locationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating location row failed, no ID obtained.");
                }

                int bikerId = getBikerIdByEmail(connection, biker.getEmail());
                if (bikerId == -1)
                    throw new SQLException(String.format("The biker %s does not exist in data base.", biker.getEmail()));

                String saveBikerLocationQuery = "insert into la_buena_db.biker_location (id, id_biker, id_location) values (0, ?, ?);";
                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement(saveBikerLocationQuery);
                preparedStatement.setInt(1, bikerId);
                preparedStatement.setInt(2, locationId);
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
    public void updateToken(Biker biker) throws InternalServerErrorException {
        try {
            Connection connection = connectionProvider.get();
            try {
                String saveBikerQuery = "update la_buena_db.biker set cloud_messaging_token = ?, updated_at = NOW() " +
                        "where email = ?;";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveBikerQuery);
                preparedStatement.setString(1, biker.getGcmToken());
                preparedStatement.setString(2, biker.getEmail());
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
    public boolean isEmailFromBiker(String email) throws InternalServerErrorException {
        Connection connection = connectionProvider.get();
        try {
            String bikersQuery = "select count(*) as count from la_buena_db.biker where biker.email = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(bikersQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean emailFromBiker = false;

            while (resultSet.next()) {
                int count = resultSet.getInt("count");
                emailFromBiker = count > 0;
            }
            resultSet.close();
            closeConnection(connection);
            return emailFromBiker;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void updateStock(Biker biker) throws InternalServerErrorException {
        try {
            Connection connection = connectionProvider.get();
            try {
                String saveBikerQuery = "update la_buena_db.biker set stock = ?, updated_at = NOW() " +
                        "where email = ?;";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveBikerQuery);
                preparedStatement.setInt(1, biker.getLastStock());
                preparedStatement.setString(2, biker.getEmail());
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


    public int getBikerIdByEmail(Connection conn, String email) throws InternalServerErrorException {
        int bikerId = -1;
        try {
            String bikersQuery = "select id_biker from la_buena_db.biker where email=?";
            PreparedStatement preparedStatement = conn.prepareStatement(bikersQuery);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                bikerId = rs.getInt("id_biker");
                break;
            }
            rs.close();

            return bikerId;
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * Implements the bike driver selection based in biker work load.
     *
     * @param coordinates Client location to deliver.
     * @return Biker to deliver an order.
     */
    @Override
    public Biker selectDriver(Coordinates coordinates) throws InternalServerErrorException {
        Connection connection = connectionProvider.get();
        try {
            Biker biker = null;
            String bikersQuery = "select biker.id_biker, biker.email, biker.name, biker.stock, " +
                    "biker.cloud_messaging_token, count(order.id_biker) from la_buena_db.biker " +
                    "  left join la_buena_db.order on biker.id_biker = order.id_biker " +
                    "  and order.delivered = 0 or order.delivered is null " +
                    "  group by biker.id_biker\n" +
                    "  order by count(order.id_biker);";
            ResultSet resultSet = connection.prepareStatement(bikersQuery).executeQuery();

            while (resultSet.next()) {
                int idBiker = resultSet.getInt("id_biker");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String token = resultSet.getString("cloud_messaging_token");
                int stock = resultSet.getInt("stock");

                biker = new Biker(idBiker, name, email, token, stock);
            }
            resultSet.close();
            closeConnection(connection);
            return biker;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
