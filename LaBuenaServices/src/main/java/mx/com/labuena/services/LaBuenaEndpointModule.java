package mx.com.labuena.services;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;

import java.util.HashSet;
import java.util.Set;

import mx.com.labuena.services.resources.BikersEndpoint;
import mx.com.labuena.services.resources.BranchesEndpoint;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuenaEndpointModule extends GuiceSystemServiceServletModule {
    @Override
    protected void configureServlets() {
        super.configureServlets();
        Set<Class<?>> serviceClasses = new HashSet<>();
        serviceClasses.add(BikersEndpoint.class);
        serviceClasses.add(BranchesEndpoint.class);
        this.serveGuiceSystemServiceServlet("/_ah/spi/*", serviceClasses);
    }
}
