package mx.com.labuena.bikedriver.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by clerks on 8/13/16.
 */

public class Order {
    private int quantity;
    private int orderId;
    private String clientEmail;
    private String clientName;
    private int bikerId;
    private int clientId;
    private Coordinate coordinates;

    public Order() {
    }

    public Order(int orderId, String clientEmail, String clientName, Coordinate coordinates, int quantity) {
        this.orderId = orderId;
        this.clientEmail = clientEmail;
        this.clientName = clientName;
        this.coordinates = coordinates;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
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

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
