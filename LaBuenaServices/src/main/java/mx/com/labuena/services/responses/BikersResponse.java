package mx.com.labuena.services.responses;

import java.util.List;

import mx.com.labuena.services.tos.Biker;

/**
 * Created by moracl6 on 8/1/2016.
 */

public class BikersResponse {
    private List<Biker> bikers;

    public BikersResponse() {
    }

    public BikersResponse(List<Biker> bikers) {
        this.bikers = bikers;
    }

    public List<Biker> getBikers() {
        return bikers;
    }

    public void setBikers(List<Biker> bikers) {
        this.bikers = bikers;
    }
}
