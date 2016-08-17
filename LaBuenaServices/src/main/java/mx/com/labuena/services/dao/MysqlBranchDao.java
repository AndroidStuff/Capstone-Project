package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.models.Branch;
import mx.com.labuena.services.models.BranchDao;
import mx.com.labuena.services.models.Stock;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class MysqlBranchDao extends BaseDao implements BranchDao {
    private static final Logger log = Logger.getLogger(MysqlBranchDao.class.getName());

    @Inject
    public MysqlBranchDao(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public List<Branch> getAll() throws InternalServerErrorException {
        List<Branch> branches = new ArrayList<>();
        Connection connection = connectionProvider.get();
        try {
            String branchesQuery = "select name, email from la_buena_db.branch";
            ResultSet rs = connection.prepareStatement(branchesQuery).executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");

                branches.add(new Branch(email, name));
            }
            rs.close();
            closeConnection(connection);
            return branches;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void save(Branch branch) throws InternalServerErrorException {
        try {
            Connection connection = connectionProvider.get();
            try {

                String saveBranchQuery = "insert into la_buena_db.branch (id_branch, email, name) values (0, ?, ?);";
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(saveBranchQuery);
                preparedStatement.setString(1, branch.getEmail());
                preparedStatement.setString(2, branch.getName());
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
    public Stock getStock() throws InternalServerErrorException {
        Stock stock = new Stock();
        Connection connection = connectionProvider.get();
        try {
            String bikersStockQuery = "select sum(stock) from la_buena_db.biker;";
            ResultSet bikersStockResultSet = connection.prepareStatement(bikersStockQuery).executeQuery();

            int bikersStock = 0;

            while (bikersStockResultSet.next()) {
                bikersStock = bikersStockResultSet.getInt(1);
            }
            bikersStockResultSet.close();
            stock.setExternalStock(bikersStock);

            String ordersToDeliverQuery = "select sum(quantity) from la_buena_db.order where delivered = 0;";
            ResultSet ordersToDeliverResultSet = connection.prepareStatement(ordersToDeliverQuery).executeQuery();

            int ordersToDeliver = 0;

            while (ordersToDeliverResultSet.next()) {
                ordersToDeliver = ordersToDeliverResultSet.getInt(1);
            }
            ordersToDeliverResultSet.close();

            stock.setOrdersToDeliver(ordersToDeliver);
            closeConnection(connection);
            return stock;
        } catch (SQLException e) {
            closeConnection(connection);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
