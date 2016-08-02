package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.tos.Biker;
import mx.com.labuena.services.tos.Location;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class MysqlBikerDao extends BaseDao implements BikerDao {
    private static final Logger log = Logger.getLogger(MysqlBikerDao.class.getName());

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

                Location lastLocation = new Location();
                bikers.add(new Biker(name, email, phone, lastLocation, stock));
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

                String saveBikerQuery = "insert into la_buena_db.biker values (0, ?, ?, 0, ?, ?);";
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
}
