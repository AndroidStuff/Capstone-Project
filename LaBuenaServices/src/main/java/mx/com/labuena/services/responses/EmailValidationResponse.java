package mx.com.labuena.services.responses;

/**
 * Created by clerks on 8/18/16.
 */

public class EmailValidationResponse {
    private boolean validEmail;

    public EmailValidationResponse() {
    }

    public EmailValidationResponse(boolean validEmail) {
        this.validEmail = validEmail;
    }

    public boolean isValidEmail() {
        return validEmail;
    }

    public void setValidEmail(boolean validEmail) {
        this.validEmail = validEmail;
    }
}
