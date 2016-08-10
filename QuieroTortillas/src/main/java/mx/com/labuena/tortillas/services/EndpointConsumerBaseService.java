package mx.com.labuena.tortillas.services;

import android.app.IntentService;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by clerks on 8/9/16.
 */

public abstract class EndpointConsumerBaseService extends IntentService {
    private static final String TAG = EndpointConsumerBaseService.class.getSimpleName();

    public static final String URL_FORMAT = "https://%s.appspot.com/_ah/api/";
    public static final String PROPERTIES_PATH = "configuration.properties";
    public static final String GC_PROJECT_ID_PROPERTY = "google_cloud_project_id";

    public EndpointConsumerBaseService(String name) {
        super(name);
    }

    protected String getRootUrl() {
        return String.format(URL_FORMAT, getProjectId());
    }

    @NonNull
    protected String getProjectId() {
        Properties properties = getProperties();
        return properties.getProperty(GC_PROJECT_ID_PROPERTY);
    }

    private Properties getProperties() {
        Properties properties = new Properties();

        try {
            AssetManager assetManager = this.getAssets();
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

    protected static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }


}
