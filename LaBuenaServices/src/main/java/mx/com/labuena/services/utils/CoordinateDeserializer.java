package mx.com.labuena.services.utils;

import com.google.appengine.repackaged.org.codehaus.jackson.JsonParser;
import com.google.appengine.repackaged.org.codehaus.jackson.map.DeserializationContext;
import com.google.appengine.repackaged.org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class CoordinateDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        return new BigDecimal(value).setScale(6, BigDecimal.ROUND_HALF_UP);
    }
}
