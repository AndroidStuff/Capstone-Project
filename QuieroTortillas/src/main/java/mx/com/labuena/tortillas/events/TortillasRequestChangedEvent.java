package mx.com.labuena.tortillas.events;

import mx.com.labuena.tortillas.models.TortillasRequest;

/**
 * Created by clerks on 8/9/16.
 */

public class TortillasRequestChangedEvent {
    private final TortillasRequest tortillasRequest;

    public TortillasRequestChangedEvent(TortillasRequest tortillasRequest) {
        this.tortillasRequest = tortillasRequest;
    }

    public TortillasRequest getTortillasRequest() {
        return tortillasRequest;
    }
}
