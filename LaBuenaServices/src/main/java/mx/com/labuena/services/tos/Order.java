package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/1/2016.
 */
public class Order {
    private String clientEmail;
    private int quantity;

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
}
