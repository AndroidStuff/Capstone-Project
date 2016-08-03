package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class Client {
    private String email;
    private String name;

    private Location coordinates;

    public Client() {
    }

    public Client(String email, String name, Location coordinates) {
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

    public Location getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Location coordinates) {
        this.coordinates = coordinates;
    }
}
