package mx.com.labuena.services.models;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

/**
 * Created by moracl6 on 8/2/2016.
 */

public interface BranchDao {
    List<Branch> getAll() throws InternalServerErrorException;

    void save(Branch branch) throws InternalServerErrorException;

    Stock getStock() throws InternalServerErrorException;

    boolean isEmailFromBranch(String email) throws InternalServerErrorException;
}
