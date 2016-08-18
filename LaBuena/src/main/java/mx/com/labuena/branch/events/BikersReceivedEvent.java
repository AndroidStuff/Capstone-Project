package mx.com.labuena.branch.events;

import java.util.List;

import mx.com.labuena.services.bikers.model.Biker;

/**
 * Created by clerks on 8/17/16.
 */

public class BikersReceivedEvent {
    private final List<Biker> bikers;

    public BikersReceivedEvent(List<Biker> bikers) {
        this.bikers = bikers;
    }

    public List<Biker> getBikers() {
        return bikers;
    }
}
