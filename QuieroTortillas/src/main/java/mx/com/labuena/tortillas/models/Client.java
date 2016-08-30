package mx.com.labuena.tortillas.models;

import android.util.Patterns;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class Client {
    public static final Pattern PASSWORD = Pattern.compile("^[a-zA-Z0-9]{6,12}$");

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
        return userInputData() && isValidEmail() && isValidPassword();
    }

    public boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword() {
        return PASSWORD.matcher(password).matches();
    }

    private boolean userInputData() {
        return StringUtils.isNoneBlank(name) && StringUtils.isNoneBlank(email) && StringUtils.isNoneBlank(password);
    }
}

