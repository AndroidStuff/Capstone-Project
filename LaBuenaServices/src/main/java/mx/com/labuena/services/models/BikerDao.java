package mx.com.labuena.services.models;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.util.List;

/**
 * Created by moracl6 on 8/2/2016.
 */

public interface BikerDao {
    List<Biker> getAll() throws InternalServerErrorException;

    void save(Biker biker) throws InternalServerErrorException;

    void saveLocation(Biker biker) throws InternalServerErrorException;
}
