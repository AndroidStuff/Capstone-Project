package mx.com.labuena.services.resources;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.util.List;

import mx.com.labuena.services.dao.ClientDao;
import mx.com.labuena.services.dao.OrderDao;
import mx.com.labuena.services.messaging.Message;
import mx.com.labuena.services.messaging.MessageNotifier;
import mx.com.labuena.services.messaging.MessageWithSingleReceiver;
import mx.com.labuena.services.responses.ClientsResponse;
import mx.com.labuena.services.tos.BikeDriverSelector;
import mx.com.labuena.services.tos.Biker;
import mx.com.labuena.services.tos.Client;
import mx.com.labuena.services.tos.Order;
import mx.com.labuena.services.tos.OrderNotification;


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

    @Inject
    private OrderDao orderDao;

    @Inject
    private MessageNotifier messageNotifier;

    @Inject
    private BikeDriverSelector bikeDriverSelector;

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

    @ApiMethod(name = "requestTortillas",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void requestTortillas(Order order) throws InternalServerErrorException {
        int orderId = orderDao.save(order);
        Client client = clientDao.findByEmail(order.getClientEmail());
        Biker biker = bikeDriverSelector.selectDriver(client);

        OrderNotification orderNotification = new OrderNotification(orderId, order.getQuantity(),
                client.getCoordinates());
        Message<OrderNotification> message =
                new MessageWithSingleReceiver<>(biker.getGcmToken(), orderNotification);
        messageNotifier.sendMessage(message);
    }
}
