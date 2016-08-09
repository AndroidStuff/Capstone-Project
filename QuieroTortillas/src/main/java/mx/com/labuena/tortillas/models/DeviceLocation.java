package mx.com.labuena.tortillas.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by clerks on 8/9/16.
 */

public class DeviceLocation implements Parcelable {
    private final double latitude;
    private final double longitude;

    public DeviceLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected DeviceLocation(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<DeviceLocation> CREATOR = new Creator<DeviceLocation>() {
        @Override
        public DeviceLocation createFromParcel(Parcel source) {
            return new DeviceLocation(source);
        }

        @Override
        public DeviceLocation[] newArray(int size) {
            return new DeviceLocation[size];
        }
    };
}
