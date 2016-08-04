package mx.com.labuena.services.models;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class OrderNotification {
    private int quantity;
    private Coordinates coordinates;
    private int orderId;

    public OrderNotification() {
    }

    public OrderNotification(int orderId, int quantity, Coordinates coordinates) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.coordinates = coordinates;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
