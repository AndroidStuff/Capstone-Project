package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

import mx.com.labuena.services.tos.Branch;

/**
 * Created by moracl6 on 8/2/2016.
 */

public interface BranchDao {
    List<Branch> getAll() throws InternalServerErrorException;
    void save(Branch branch) throws InternalServerErrorException;
}
