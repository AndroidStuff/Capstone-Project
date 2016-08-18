package mx.com.labuena.branch.events;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class ResetPasswordFailureEvent {
    private final String email;

    public ResetPasswordFailureEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
