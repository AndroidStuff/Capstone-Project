/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package mx.com.labuena.services.resources;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.util.List;

import mx.com.labuena.services.models.Biker;
import mx.com.labuena.services.models.BikerDao;
import mx.com.labuena.services.models.Order;
import mx.com.labuena.services.models.OrderDao;
import mx.com.labuena.services.responses.BikersResponse;
import mx.com.labuena.services.responses.OrdersResponse;

@Api(
        name = "bikers",
        version = "v2",
        namespace = @ApiNamespace(
                ownerDomain = "services.labuena.com.mx",
                ownerName = "services.labuena.com.mx",
                packagePath = ""
        )
)
public class BikersEndpoint {
    @Inject
    private BikerDao bikerDao;

    @Inject
    private OrderDao orderDao;

    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Biker biker) throws InternalServerErrorException {
        bikerDao.save(biker);
    }

    @ApiMethod(name = "location",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void saveLocation(Biker biker) throws InternalServerErrorException {
        bikerDao.saveLocation(biker);
    }

    @ApiMethod(name = "updateToken",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void updateToken(Biker biker) throws InternalServerErrorException {
        bikerDao.updateToken(biker);
    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BikersResponse getAll() throws InternalServerErrorException {
        List<Biker> bikers = bikerDao.getAll();
        return new BikersResponse(bikers);
    }

    @ApiMethod(name = "ordersToDeliver",
            httpMethod = ApiMethod.HttpMethod.GET)
    public OrdersResponse getOrdersToDeliver(@Named("bikerId") int bikerId) throws InternalServerErrorException {
        List<Order> orders = orderDao.findByBikerId(bikerId);
        return new OrdersResponse(orders);
    }
}
