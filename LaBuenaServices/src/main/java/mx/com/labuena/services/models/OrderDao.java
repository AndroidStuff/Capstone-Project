package mx.com.labuena.services.models;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

/**
 * Created by moracl6 on 8/4/2016.
 */

public interface OrderDao {
    int save(Order order) throws InternalServerErrorException;

    List<Order> findByBikerId(int bikerId) throws InternalServerErrorException;

    void updateOrder(Order order) throws InternalServerErrorException;
}
