package mx.com.labuena.tortillas.events;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class ResetPasswordSuccessfulEvent {
    private final String email;

    public ResetPasswordSuccessfulEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
