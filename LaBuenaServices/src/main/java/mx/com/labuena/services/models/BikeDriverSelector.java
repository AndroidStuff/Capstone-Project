package mx.com.labuena.services.models;

/**
 * Allows to select a bike driver to deliver an order.
 * Created by moracl6 on 8/4/2016.
 */

public interface BikeDriverSelector {
    Biker selectDriver(Client client);
}
