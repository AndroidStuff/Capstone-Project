package mx.com.labuena.bikedriver.models;

import android.content.SharedPreferences;

/**
 * Created by moracl6 on 8/8/2016.
 */

public class SharedPreferencesRepository implements PreferencesRepository {
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void save(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    @Override
    public void save(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    @Override
    public String read(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public boolean read(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

}
