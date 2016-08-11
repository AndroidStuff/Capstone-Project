package mx.com.labuena.tortillas.models;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class Client {
    private final String name;
    private final String email;
    private final String password;

    public Client(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValid() {
        return StringUtils.isNoneBlank(name) && StringUtils.isNoneBlank(email) && StringUtils.isNoneBlank(password);
    }
}

