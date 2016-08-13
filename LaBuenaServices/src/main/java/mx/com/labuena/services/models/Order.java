package mx.com.labuena.services.models;

/**
 * Created by moracl6 on 8/1/2016.
 */
public class Order {
    private String clientEmail;
    private String clientName;
    private int quantity;
    private int bikerId;
    private int clientId;
    private Coordinates coordinates;
    private int orderId;

    public Order(String clientEmail, int quantity) {
        this.clientEmail = clientEmail;
        this.quantity = quantity;
    }

    public Order() {
    }

    public Order(int orderId, String email, String name, Coordinates coordinates, int quantity) {
        this.orderId = orderId;
        this.clientEmail = email;
        this.clientName = name;
        this.coordinates = coordinates;
        this.quantity = quantity;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
