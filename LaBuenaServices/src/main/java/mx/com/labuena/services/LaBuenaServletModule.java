package mx.com.labuena.services;

import com.google.inject.servlet.ServletModule;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuenaServletModule extends ServletModule {
    /**
     * Register filters and servlets for LaBuena
     */
    @Override protected void configureServlets() {
        // serve("/*").with(LaBuenaServletClass.class);
    }
}
