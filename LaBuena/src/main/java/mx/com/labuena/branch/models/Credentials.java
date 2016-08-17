package mx.com.labuena.branch.models;


import org.apache.commons.lang3.StringUtils;

/**
 * Created by clerks on 8/6/16.
 */

public class Credentials {
    private final String email;
    private final String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValid() {
        return StringUtils.isNoneBlank(email) && StringUtils.isNoneBlank(password);
    }
}
