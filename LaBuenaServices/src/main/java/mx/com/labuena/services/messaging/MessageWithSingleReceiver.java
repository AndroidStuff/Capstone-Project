package mx.com.labuena.services.messaging;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class MessageWithSingleReceiver<T> extends Message<T> {
    private final String to;

    public MessageWithSingleReceiver(String to, T body) {
        super(body);
        this.to = to;
    }

    public String getTo() {
        return to;
    }
}
