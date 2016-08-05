package mx.com.labuena.services.models;

/**
 * Created by moracl6 on 8/1/2016.
 */
public class Order {
    private String clientEmail;
    private int quantity;
    private int bikerId;
    private int clientId;
    private Coordinates coordinates;

    public Order(String clientEmail, int quantity) {
        this.clientEmail = clientEmail;
        this.quantity = quantity;
    }

    public Order() {
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBikerId() {
        return bikerId;
    }

    public void setBikerId(int bikerId) {
        this.bikerId = bikerId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
