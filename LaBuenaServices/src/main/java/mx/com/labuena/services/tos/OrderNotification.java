package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class OrderNotification {
    private int quantity;
    private Coordinates coordinates;

    public OrderNotification() {
    }

    public OrderNotification(int quantity, Coordinates coordinates) {
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
}
