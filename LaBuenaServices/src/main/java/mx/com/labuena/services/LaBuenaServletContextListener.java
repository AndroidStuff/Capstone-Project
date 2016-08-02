package mx.com.labuena.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Triggered as soon as the application is deployed, and before any request arrives.
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuenaServletContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new LaBuenaEndpointModule(),
                new LaBuenaServletModule(), new LaBuendaDependencyModule());
    }
}
