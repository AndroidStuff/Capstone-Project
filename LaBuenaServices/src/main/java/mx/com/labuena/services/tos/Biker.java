package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/1/2016.
 */

public class Biker {
    private String name;
    private String email;
    private String phone;
    private Location lastLocation;
    private int lastStock;

    public Biker() {
    }

    public Biker(String name, String email, String phone, Location lastLocation, int lastStock) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.lastLocation = lastLocation;
        this.lastStock = lastStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public int getLastStock() {
        return lastStock;
    }

    public void setLastStock(int lastStock) {
        this.lastStock = lastStock;
    }
}
