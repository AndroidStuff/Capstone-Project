/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package mx.com.labuena.services;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mx.com.labuena.services.responses.BikersResponse;
import mx.com.labuena.services.tos.Biker;
import mx.com.labuena.services.tos.Location;

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
    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Biker biker) {

    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BikersResponse getAll() {
        List<Biker> bikers = new ArrayList<>();
        Location location = new Location(new BigDecimal("47.6"), new BigDecimal("122.33"));
        bikers.add(new Biker("Clemente", "morales.fernandez.clemente@gmail.com", "7347896184", location, 35));

        Location location2 = new Location(new BigDecimal("47.6"), new BigDecimal("122.33"));
        bikers.add(new Biker("William", "morales.fernandez.williams@gmail.com", "73478145184", location2, 35));
        return new BikersResponse(bikers);
    }
}
