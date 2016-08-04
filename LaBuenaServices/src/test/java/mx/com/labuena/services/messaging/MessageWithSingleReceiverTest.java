package mx.com.labuena.services.messaging;

import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectWriter;
import com.google.appengine.repackaged.org.codehaus.jackson.type.TypeReference;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import mx.com.labuena.services.tos.Coordinates;
import mx.com.labuena.services.tos.OrderNotification;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class MessageWithSingleReceiverTest {
    private static final String DUMMY_JSON_COORDINATES = "{\"to\":\"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...\"," +
            "\"data\":{\"quantity\":5," +
            "\"coordinates\":{\"latitude\":\"60.064840\",\"longitude\":\"-135.878906\"}}}";
    public static final String DUMMY_RECEIVER = "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...";
    public static final String DUMMY_LATITUDE = "60.064840";
    public static final String DUMMY_LONGITUDE = "-135.878906";
    public static final int DUMMY_TORTILAS_QUANTITY = 5;

    @Test
    public void message_with_single_receiver_includes_body_and_receiver() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithType(new TypeReference<MessageWithSingleReceiver<OrderNotification>>() {
        });
        MessageWithSingleReceiver<OrderNotification> orderNotification = buildDummyOrderNotification();
        String jsonCoordinates = writer.writeValueAsString(orderNotification);

        Assert.assertEquals(DUMMY_JSON_COORDINATES, jsonCoordinates);
    }

    private MessageWithSingleReceiver<OrderNotification> buildDummyOrderNotification() {
        OrderNotification orderNotification = new OrderNotification(DUMMY_TORTILAS_QUANTITY,
                new Coordinates(new BigDecimal(DUMMY_LATITUDE).setScale(6, BigDecimal.ROUND_HALF_UP),
                        new BigDecimal(DUMMY_LONGITUDE).setScale(6, BigDecimal.ROUND_HALF_UP)));

        return new MessageWithSingleReceiver(DUMMY_RECEIVER, orderNotification);
    }
}
