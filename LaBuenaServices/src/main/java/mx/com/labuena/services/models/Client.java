package mx.com.labuena.services.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by moracl6 on 8/3/2016.
 */

public class Client {
    private int clientId;
    private String email;
    private String name;
    private String fcmToken;

    public Client() {
    }

    public Client(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public Client(int clientId , String email, String name) {
        this(email, name);
        this.clientId = clientId;
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

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
