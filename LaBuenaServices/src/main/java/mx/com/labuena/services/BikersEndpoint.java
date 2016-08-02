/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package mx.com.labuena.services;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.util.List;

import mx.com.labuena.services.dao.BikerDao;
import mx.com.labuena.services.responses.BikersResponse;
import mx.com.labuena.services.tos.Biker;

@Api(
        name = "bikers",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "services.labuena.com.mx",
                ownerName = "services.labuena.com.mx",
                packagePath = ""
        )
)
public class BikersEndpoint {
    @Inject
    private BikerDao bikerDao;

    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Biker biker) {

    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BikersResponse getAll() throws InternalServerErrorException {
        List<Biker> bikers = bikerDao.getAll();
        return new BikersResponse(bikers);
    }
}
