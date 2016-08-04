package mx.com.labuena.services.models;

import java.util.Date;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BikerLocation {
    private Date readAt;
    private Coordinates coordinates;

    public BikerLocation() {

    }

    public BikerLocation(Date readAt, Coordinates coordinates) {
        this.readAt = readAt;
        this.coordinates = coordinates;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
