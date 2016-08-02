package mx.com.labuena.services.dao;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

import mx.com.labuena.services.tos.Biker;

/**
 * Created by moracl6 on 8/2/2016.
 */

public interface BikerDao {
    List<Biker> getAll() throws InternalServerErrorException;
}
