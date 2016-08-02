package mx.com.labuena.services.tos;

/**
 * Created by moracl6 on 8/1/2016.
 */

public class Biker {
    private int branchId;
    private String name;
    private String email;
    private String phone;
    private BikerLocation bikerLocation;
    private int lastStock;

    public Biker() {
    }

    public Biker(String name, String email, String phone, int lastStock) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public int getLastStock() {
        return lastStock;
    }

    public void setLastStock(int lastStock) {
        this.lastStock = lastStock;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public BikerLocation getBikerLocation() {
        return bikerLocation;
    }

    public void setBikerLocation(BikerLocation bikerLocation) {
        this.bikerLocation = bikerLocation;
    }
}
