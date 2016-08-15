package mx.com.labuena.services.resources;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.messaging.Message;
import mx.com.labuena.services.messaging.MessageNotifier;
import mx.com.labuena.services.messaging.MessageWithSingleReceiver;
import mx.com.labuena.services.models.BikeDriverSelector;
import mx.com.labuena.services.models.Biker;
import mx.com.labuena.services.models.Client;
import mx.com.labuena.services.models.ClientDao;
import mx.com.labuena.services.models.Order;
import mx.com.labuena.services.models.OrderDao;
import mx.com.labuena.services.responses.ClientsResponse;


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
    private static final Logger log = Logger.getLogger(ClientsEndpoint.class.getName());

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
        log.log(Level.INFO, "Saving client " + client);
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
        Client client = clientDao.findByEmail(order.getClientEmail());
        Biker biker = bikeDriverSelector.selectDriver(order.getCoordinates());
        log.log(Level.INFO, String.format("Selected biker %s to deliver order %s", biker, order));
        order.setClientId(client.getClientId());
        order.setBikerId(biker.getBikerId());
        int orderId = orderDao.save(order);
        order.setOrderId(orderId);
        sendNotification(client.getFcmToken(), order);
        sendNotification(biker.getGcmToken(), order);
    }

    private void sendNotification(String receiverToken, Order order)
            throws InternalServerErrorException {
        Message<Order> message =
                new MessageWithSingleReceiver<>(receiverToken, order);
        messageNotifier.sendMessage(message);
    }
}
