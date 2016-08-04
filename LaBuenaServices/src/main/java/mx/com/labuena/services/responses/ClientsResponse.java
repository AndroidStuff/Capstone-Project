package mx.com.labuena.services.responses;

import java.util.List;

import mx.com.labuena.services.models.Client;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class ClientsResponse {
    private List<Client> clients;

    public ClientsResponse() {
    }

    public ClientsResponse(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
