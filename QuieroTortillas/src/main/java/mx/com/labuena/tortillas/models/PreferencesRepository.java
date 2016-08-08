package mx.com.labuena.tortillas.models;

/**
 * Created by moracl6 on 8/8/2016.
 */

public interface PreferencesRepository {
    void save(String key, boolean value);
    void save(String key, String value);

    String read(String key, String defaultValue);
    boolean read(String key, boolean defaultValue);
}
