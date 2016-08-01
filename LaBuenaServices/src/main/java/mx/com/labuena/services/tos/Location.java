package mx.com.labuena.services.tos;

import java.math.BigDecimal;

/**
 * Created by moracl6 on 8/1/2016.
 */

public class Location {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Location() {
    }

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
