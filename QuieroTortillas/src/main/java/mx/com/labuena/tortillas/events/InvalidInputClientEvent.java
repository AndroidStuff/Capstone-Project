package mx.com.labuena.tortillas.events;

import mx.com.labuena.tortillas.models.Client;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class InvalidInputClientEvent {
    private final Client client;

    public InvalidInputClientEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
