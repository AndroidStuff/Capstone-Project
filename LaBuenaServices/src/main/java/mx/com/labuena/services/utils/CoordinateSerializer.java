package mx.com.labuena.services.utils;


import com.google.appengine.repackaged.org.codehaus.jackson.JsonGenerator;
import com.google.appengine.repackaged.org.codehaus.jackson.map.JsonSerializer;
import com.google.appengine.repackaged.org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class CoordinateSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider provider) throws
            IOException {
        jsonGenerator.writeString(value.setScale(6, BigDecimal.ROUND_HALF_UP).toString());
    }
}
