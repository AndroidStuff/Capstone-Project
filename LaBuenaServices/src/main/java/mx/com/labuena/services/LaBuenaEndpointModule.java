package mx.com.labuena.services;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuenaEndpointModule extends GuiceSystemServiceServletModule {
    @Override
    protected void configureServlets() {
        super.configureServlets();
        Set<Class<?>> serviceClasses = new HashSet<>();
        serviceClasses.add(BikersEndpoint.class);
        this.serveGuiceSystemServiceServlet("/_ah/spi/*", serviceClasses);
    }
}
