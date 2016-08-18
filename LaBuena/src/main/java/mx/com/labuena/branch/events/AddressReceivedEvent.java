package mx.com.labuena.branch.events;

/**
 * Created by clerks on 8/14/16.
 */

public class AddressReceivedEvent {
    private final String address;

    public AddressReceivedEvent(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
