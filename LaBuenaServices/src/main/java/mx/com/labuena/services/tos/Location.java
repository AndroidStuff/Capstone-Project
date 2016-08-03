package mx.com.labuena.services.tos;

import com.google.appengine.repackaged.org.codehaus.jackson.map.annotate.JsonDeserialize;
import com.google.appengine.repackaged.org.codehaus.jackson.map.annotate.JsonSerialize;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

import mx.com.labuena.services.utils.CoordinateDeserializer;
import mx.com.labuena.services.utils.CoordinateSerializer;

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

    @JsonSerialize(using = CoordinateSerializer.class)
    public BigDecimal getLatitude() {
        return latitude;
    }

    @JsonDeserialize(using = CoordinateDeserializer.class)
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @JsonSerialize(using = CoordinateSerializer.class)
    public BigDecimal getLongitude() {
        return longitude;
    }

    @JsonDeserialize(using = CoordinateDeserializer.class)
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(latitude, location.latitude)
                .append(longitude, location.longitude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }
}
