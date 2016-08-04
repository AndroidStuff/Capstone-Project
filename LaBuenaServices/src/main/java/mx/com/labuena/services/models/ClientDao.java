package mx.com.labuena.services.models;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

/**
 * Created by moracl6 on 8/3/2016.
 */

public interface ClientDao {
    List<Client> getAll() throws InternalServerErrorException;
    void save(Client client) throws InternalServerErrorException;
    Client findByEmail(String email) throws InternalServerErrorException;
}
