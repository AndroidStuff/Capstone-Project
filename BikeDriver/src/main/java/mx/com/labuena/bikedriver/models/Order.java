package mx.com.labuena.bikedriver.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by clerks on 8/13/16.
 */

public class Order implements Parcelable {
    private int quantity;
    private int orderId;
    private String clientEmail;
    private String clientName;
    private int bikerId;
    private int clientId;
    private Coordinate coordinates;

    public Order() {
    }

    public Order(int orderId, String clientEmail, String clientName, Coordinate coordinates, int quantity) {
        this.orderId = orderId;
        this.clientEmail = clientEmail;
        this.clientName = clientName;
        this.coordinates = coordinates;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getBikerId() {
        return bikerId;
    }

    public void setBikerId(int bikerId) {
        this.bikerId = bikerId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quantity);
        dest.writeInt(this.orderId);
        dest.writeString(this.clientEmail);
        dest.writeString(this.clientName);
        dest.writeInt(this.bikerId);
        dest.writeInt(this.clientId);
        dest.writeParcelable(this.coordinates, flags);
    }

    protected Order(Parcel in) {
        this.quantity = in.readInt();
        this.orderId = in.readInt();
        this.clientEmail = in.readString();
        this.clientName = in.readString();
        this.bikerId = in.readInt();
        this.clientId = in.readInt();
        this.coordinates = in.readParcelable(Coordinate.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
