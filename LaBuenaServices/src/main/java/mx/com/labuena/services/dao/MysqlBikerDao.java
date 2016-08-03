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

import javax.sql.DataSource;

import mx.com.labuena.services.tos.Biker;
import mx.com.labuena.services.tos.BikerLocation;
import mx.com.labuena.services.tos.Location;
import mx.com.labuena.services.utils.DateExtensor;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class MysqlBikerDao extends BaseDao implements BikerDao {
    private static final Logger log = Logger.getLogger(MysqlBikerDao.class.getName());

    @Inject
    public MysqlBikerDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Biker> getAll() throws InternalServerErrorException {
        List<Biker> bikers = new ArrayList<>();
        Connection conn = openConnection();

        try {
            String bikersQuery = "select name, email, phone, stock from la_buena_db.biker";
            ResultSet rs = conn.prepareStatement(bikersQuery).executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String name = rs.getString("name");
                int stock = rs.getInt("stock");
                bikers.add(new Biker(name, email, phone, stock));
            }
            rs.close();
            closeConnection(conn);
            return bikers;
        } catch (SQLException e) {
            closeConnection(conn);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void save(Biker biker) throws InternalServerErrorException {
        Connection connection = openConnection();
        try {
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
        Connection connection = openConnection();
        try {
            try {
                BikerLocation bikerLocation = biker.getBikerLocation();
                Location location = bikerLocation.getLocation();
                String saveLocationQuery = "insert into la_buena_db.location (id_location, latitude, longitude, created_at) values (0, ?, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveLocationQuery,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setBigDecimal(1, location.getLatitude());
                preparedStatement.setBigDecimal(2, location.getLongitude());
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
}
