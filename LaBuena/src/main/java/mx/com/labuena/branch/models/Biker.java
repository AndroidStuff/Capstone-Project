package mx.com.labuena.branch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by moracl6 on 8/18/2016.
 */

public class Biker implements Parcelable {
    private final String name;
    private final String email;
    private final String password;
    private final String phone;
    private int lastStock;
    private Coordinates lastLocation;


    public Biker(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public Biker(String name, String email, String password, String phone, int lastStock) {
        this(name, email, password, phone);
        this.lastStock = lastStock;
    }

    public Biker(String name, String email, String phone, int lastStock,
                 Coordinates lastLocation) {
        this(name, email, StringUtils.EMPTY, phone, lastStock);
        this.lastLocation = lastLocation;
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

    public String getPhone() {
        return phone;
    }

    public int getLastStock() {
        return lastStock;
    }

    public void setLastStock(int lastStock) {
        this.lastStock = lastStock;
    }

    public Coordinates getLastLocation() {
        return lastLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.phone);
        dest.writeInt(this.lastStock);
        dest.writeParcelable(lastLocation, flags);
    }

    protected Biker(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.phone = in.readString();
        this.lastStock = in.readInt();
        lastLocation = in.readParcelable(Coordinates.class.getClassLoader());
    }

    public static final Creator<Biker> CREATOR = new Creator<Biker>() {
        @Override
        public Biker createFromParcel(Parcel source) {
            return new Biker(source);
        }

        @Override
        public Biker[] newArray(int size) {
            return new Biker[size];
        }
    };

    public boolean isValid() {
        return StringUtils.isNoneBlank(name) && StringUtils.isNoneBlank(password)
                && StringUtils.isNoneBlank(phone) && StringUtils.isNoneBlank(email);
    }

    public boolean hasReportedLocation() {
        return lastLocation != null && lastLocation.getLatitude() != 0;
    }
}
