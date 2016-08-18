package mx.com.labuena.branch.events;

import mx.com.labuena.branch.models.Biker;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class RegistrationFailureEvent {
    private final Biker biker;

    public RegistrationFailureEvent(Biker biker) {
        this.biker = biker;
    }

    public Biker getBiker() {
        return biker;
    }
}
