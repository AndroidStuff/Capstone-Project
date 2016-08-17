package mx.com.labuena.branch.models;

/**
 * Created by moracl6 on 8/17/2016.
 */

public class User {
    private final String email;
    private  final String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
