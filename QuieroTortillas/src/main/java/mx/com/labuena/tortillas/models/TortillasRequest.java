package mx.com.labuena.tortillas.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by clerks on 8/9/16.
 */

public class TortillasRequest implements Parcelable {
    public static final int MAX_AMOUNT = 40;
    public static final int MIN_AMOUNT = 1;
    public static final int DEFAULT_CONSUME = 3;

    private int amount = DEFAULT_CONSUME;
    private final User user;
    private DeviceLocation deviceLocation;

    public TortillasRequest(User user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public DeviceLocation getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(DeviceLocation deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public boolean isAmountUnderMaxLimit() {
        return amount < MAX_AMOUNT;
    }

    public void increaseAmount() {
        ++amount;
    }

    public boolean isAmountUnderMinLimit() {
        return amount > MIN_AMOUNT;
    }

    public void decreaseAmount() {
        --amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.amount);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.deviceLocation, flags);
    }

    protected TortillasRequest(Parcel in) {
        this.amount = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.deviceLocation = in.readParcelable(DeviceLocation.class.getClassLoader());
    }

    public static final Creator<TortillasRequest> CREATOR = new Creator<TortillasRequest>() {
        @Override
        public TortillasRequest createFromParcel(Parcel source) {
            return new TortillasRequest(source);
        }

        @Override
        public TortillasRequest[] newArray(int size) {
            return new TortillasRequest[size];
        }
    };
}
