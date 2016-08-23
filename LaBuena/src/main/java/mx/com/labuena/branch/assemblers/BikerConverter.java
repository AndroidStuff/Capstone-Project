package mx.com.labuena.branch.assemblers;

import java.util.ArrayList;
import java.util.List;

import mx.com.labuena.services.bikers.model.Biker;
import mx.com.labuena.services.bikers.model.BikerLocation;
import mx.com.labuena.services.bikers.model.Coordinates;

/**
 * Created by clerks on 8/22/16.
 */

public class BikerConverter {
    public static ArrayList<mx.com.labuena.branch.models.Biker> toModel(List<Biker> bikers) {
        ArrayList<mx.com.labuena.branch.models.Biker> bikersModel = new ArrayList<>();
        for (Biker biker :
                bikers) {
            bikersModel.add(toModel(biker));
        }

        return bikersModel;
    }

    private static mx.com.labuena.branch.models.Biker toModel(Biker biker) {
        BikerLocation bikerLocation = biker.getBikerLocation();
        Coordinates coordinates = bikerLocation.getCoordinates();
        return new mx.com.labuena.branch.models.Biker(biker.getName(), biker.getEmail(), biker.getPhone(), biker.getLastStock(),
                new mx.com.labuena.branch.models.Coordinates(coordinates.getLatitude(), coordinates.getLongitude()));
    }
}
