package mx.com.labuena.services.models;

/**
 * Created by clerks on 8/16/16.
 */

public class Stock {
    private int externalStock;
    private int ordersToDeliver;

    public int getExternalStock() {
        return externalStock;
    }

    public void setExternalStock(int externalStock) {
        this.externalStock = externalStock;
    }

    public int getOrdersToDeliver() {
        return ordersToDeliver;
    }

    public void setOrdersToDeliver(int ordersToDeliver) {
        this.ordersToDeliver = ordersToDeliver;
    }
}
