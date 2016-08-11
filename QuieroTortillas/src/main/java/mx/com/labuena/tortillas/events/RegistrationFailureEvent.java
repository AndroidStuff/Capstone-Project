package mx.com.labuena.tortillas.events;

import mx.com.labuena.tortillas.models.Client;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class RegistrationFailureEvent {
    private final Client client;

    public RegistrationFailureEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
