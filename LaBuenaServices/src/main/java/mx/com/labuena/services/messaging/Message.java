package mx.com.labuena.services.messaging;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonProperty;

/**
 * Creates a message for a single receiver.
 * Created by moracl6 on 8/4/2016.
 */

public class Message<T> {
    @JsonProperty("data")
    private final T body;

    public Message(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }
}
