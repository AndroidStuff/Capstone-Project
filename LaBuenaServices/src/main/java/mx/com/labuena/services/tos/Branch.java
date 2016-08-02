package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class Branch {
    private String email;
    private String name;

    public Branch() {
    }

    public Branch(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
