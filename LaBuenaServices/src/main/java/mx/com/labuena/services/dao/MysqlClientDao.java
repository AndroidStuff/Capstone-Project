package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

import mx.com.labuena.services.tos.Client;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class MysqlClientDao implements ClientDao {
    @Override
    public List<Client> getAll() throws InternalServerErrorException {
        return null;
    }

    @Override
    public void save(Client client) throws InternalServerErrorException {

    }
}
