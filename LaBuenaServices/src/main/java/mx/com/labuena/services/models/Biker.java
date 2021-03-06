package mx.com.labuena.services.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by moracl6 on 8/1/2016.
 */

public class Biker {
    private int branchId;
    private int bikerId;
    private String name;
    private String email;
    private String phone;
    private BikerLocation bikerLocation;
    private int lastStock;
    private String gcmToken;

    public Biker() {
    }

    public Biker(String name, String email, String phone, int lastStock) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.lastStock = lastStock;
    }

    public Biker(int bikerId, String name, String email, String token, int lastStock) {
        this.bikerId = bikerId;
        this.name = name;
        this.email = email;
        this.gcmToken = token;
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

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public int getBikerId() {
        return bikerId;
    }

    public void setBikerId(int bikerId) {
        this.bikerId = bikerId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
