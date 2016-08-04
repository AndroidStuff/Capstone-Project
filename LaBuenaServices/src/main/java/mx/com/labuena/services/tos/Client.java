package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class Client {
    private String email;
    private String name;

    private Coordinates coordinates;

    public Client() {
    }

    public Client(String email, String name, Coordinates coordinates) {
        this.email = email;
        this.name = name;
        this.coordinates = coordinates;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
