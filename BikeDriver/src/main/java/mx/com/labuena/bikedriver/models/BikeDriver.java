package mx.com.labuena.bikedriver.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class BikeDriver implements Parcelable {
    private final String email;
    private final String name;
    private String fcmToken;

    public BikeDriver(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.fcmToken);
    }

    protected BikeDriver(Parcel in) {
        this.email = in.readString();
        this.name = in.readString();
        this.fcmToken = in.readString();
    }

    public static final Creator<BikeDriver> CREATOR = new Creator<BikeDriver>() {
        @Override
        public BikeDriver createFromParcel(Parcel source) {
            return new BikeDriver(source);
        }

        @Override
        public BikeDriver[] newArray(int size) {
            return new BikeDriver[size];
        }
    };
}
