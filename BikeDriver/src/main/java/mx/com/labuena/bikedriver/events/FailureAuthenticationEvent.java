package mx.com.labuena.bikedriver.events;

import mx.com.labuena.bikedriver.models.Credentials;

/**
 * Created by clerks on 8/7/16.
 */

public class FailureAuthenticationEvent {
    private final Credentials credentials;

    public FailureAuthenticationEvent(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
