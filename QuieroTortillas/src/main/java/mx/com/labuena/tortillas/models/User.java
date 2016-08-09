package mx.com.labuena.tortillas.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by moracl6 on 8/8/2016.
 */

public class User implements Parcelable {
    private final String userId;
    private final String email;
    private final String name;
    private final Uri photoUri;
    private String fcmToken;

    public User(String userId, String email, String name, Uri photoUri) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.photoUri = photoUri;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public String getName() {
        return name;
    }

    public String getFcmToken() {
        return fcmToken;
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
        dest.writeString(this.userId);
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeParcelable(this.photoUri, flags);
        dest.writeString(this.fcmToken);
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.email = in.readString();
        this.name = in.readString();
        this.photoUri = in.readParcelable(Uri.class.getClassLoader());
        fcmToken = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
