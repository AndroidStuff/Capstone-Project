package mx.com.labuena.services.resources;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.util.List;

import mx.com.labuena.services.dao.ClientDao;
import mx.com.labuena.services.responses.ClientsResponse;
import mx.com.labuena.services.tos.Client;


/**
 * Created by moracl6 on 8/3/2016.
 */
@Api(
        name = "clients",
        version = "v2",
        namespace = @ApiNamespace(
                ownerDomain = "services.labuena.com.mx",
                ownerName = "services.labuena.com.mx",
                packagePath = ""
        )
)
public class ClientsEndpoint {
    @Inject
    private ClientDao clientDao;

    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Client client) throws InternalServerErrorException {
        clientDao.save(client);
    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ClientsResponse getAll() throws InternalServerErrorException {
        List<Client> clients = clientDao.getAll();
        return new ClientsResponse(clients);
    }
}