package mx.com.labuena.services.utils;

import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectWriter;
import com.google.appengine.repackaged.org.codehaus.jackson.type.TypeReference;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import mx.com.labuena.services.tos.Location;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class CoordinateSerializerTest {
    private static final String DUMMY_JSON_COORDINATES = "{\"latitude\":\"60.064840\",\"longitude\":\"-135.878906\"}";

    @Test
    public void when_json_includes_coordinates_deserializer_applies_coordinate_deserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithType(new TypeReference<Location>() {
        });
        Location expectedLocation = buildExpectedLocation();
        String jsonCoordinates = writer.writeValueAsString(expectedLocation);

        Assert.assertEquals(DUMMY_JSON_COORDINATES, jsonCoordinates);
    }

    private Location buildExpectedLocation() {
        return new Location(new BigDecimal("60.064840").setScale(6, BigDecimal.ROUND_HALF_UP),
                new BigDecimal("-135.878906").setScale(6, BigDecimal.ROUND_HALF_UP));
    }
}
