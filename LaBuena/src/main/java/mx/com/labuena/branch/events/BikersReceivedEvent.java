package mx.com.labuena.branch.events;

import java.util.ArrayList;

import mx.com.labuena.branch.models.Biker;


/**
 * Created by clerks on 8/17/16.
 */

public class BikersReceivedEvent {
    private final ArrayList<Biker> bikers;

    public BikersReceivedEvent(ArrayList<Biker> bikers) {
        this.bikers = bikers;
    }

    public ArrayList<Biker> getBikers() {
        return bikers;
    }
}
