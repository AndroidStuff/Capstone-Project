package mx.com.labuena.branch.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by clerks on 8/13/16.
 */

public final class PropertiesReader {
    public static final String PROPERTIES_PATH = "configuration.properties";
    private static final String TAG = PropertiesReader.class.getSimpleName();

    private PropertiesReader() {
    }

    public static String getProperty(Context context, String property) {
        Properties properties = PropertiesReader.getProperties(context);
        return properties.getProperty(property);
    }

    private static Properties getProperties(Context context) {
        Properties properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(PROPERTIES_PATH);
            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return properties;
    }
}
