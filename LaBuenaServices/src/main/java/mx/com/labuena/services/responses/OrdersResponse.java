package mx.com.labuena.services.responses;

import java.util.List;

import mx.com.labuena.services.models.Order;

/**
 * Created by clerks on 8/13/16.
 */

public class OrdersResponse {
    List<Order> orders;

    public OrdersResponse() {
    }

    public OrdersResponse(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
