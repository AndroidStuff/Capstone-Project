package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import mx.com.labuena.services.tos.Order;

/**
 * Created by moracl6 on 8/4/2016.
 */

public interface OrderDao {
    int save(Order order) throws InternalServerErrorException;
}
