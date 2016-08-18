package mx.com.labuena.branch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moracl6 on 8/18/2016.
 */

public class Biker implements Parcelable {
    private final String name;
    private final String email;
    private final String password;
    private final String phone;


    public Biker(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
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
    }

    protected Biker(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.phone = in.readString();
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
        return true;
    }
}
