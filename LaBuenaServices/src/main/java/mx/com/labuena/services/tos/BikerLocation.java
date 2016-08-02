package mx.com.labuena.services.tos;

import java.util.Date;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BikerLocation {
    private Date readAt;
    private Location location;

    public BikerLocation() {

    }

    public BikerLocation(Date readAt, Location location) {
        this.readAt = readAt;
        this.location = location;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
