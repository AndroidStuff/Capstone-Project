package mx.com.labuena.services.messaging;

/**
 * Created by moracl6 on 8/4/2016.
 */

public interface MessageNotifier {
    void sendMessage(String apiKey, Message message);
}
