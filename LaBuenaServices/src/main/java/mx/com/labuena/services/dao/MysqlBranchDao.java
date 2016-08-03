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

import javax.sql.DataSource;

import mx.com.labuena.services.tos.Branch;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class MysqlBranchDao extends BaseDao implements BranchDao {
    private static final Logger log = Logger.getLogger(MysqlBranchDao.class.getName());

    @Inject
    public MysqlBranchDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public List<Branch> getAll() throws InternalServerErrorException {
        List<Branch> branches = new ArrayList<>();
        Connection conn = openConnection();

        try {
            String bikersQuery = "select name, email from la_buena_db.branch";
            ResultSet rs = conn.prepareStatement(bikersQuery).executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");

                branches.add(new Branch(email, name));
            }
            rs.close();
            closeConnection(conn);
            return branches;
        } catch (SQLException e) {
            closeConnection(conn);
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void save(Branch branch) throws InternalServerErrorException {
        Connection conn = openConnection();
        try {
            try {

                String saveBranchQuery = "insert into la_buena_db.branch (id_branch, email, name) values (0, ?, ?);";
                conn.setAutoCommit(false);
                PreparedStatement preparedStatement = conn.prepareStatement(saveBranchQuery);
                preparedStatement.setString(1, branch.getEmail());
                preparedStatement.setString(2, branch.getName());
                preparedStatement.execute();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                log.log(Level.SEVERE, e.getMessage(), e);
                throw new InternalServerErrorException(e);
            } finally {
                conn.setAutoCommit(true);
                closeConnection(conn);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
