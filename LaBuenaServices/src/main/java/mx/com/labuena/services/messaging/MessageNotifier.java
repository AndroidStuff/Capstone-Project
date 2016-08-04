package mx.com.labuena.services.messaging;

import com.google.api.server.spi.response.InternalServerErrorException;

/**
 * Created by moracl6 on 8/4/2016.
 */

public interface MessageNotifier {
    void sendMessage(Message message) throws InternalServerErrorException;
}
