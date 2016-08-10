package mx.com.labuena.tortillas.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import mx.com.labuena.services.clients.Clients;
import mx.com.labuena.services.clients.model.Client;
import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.models.PreferencesRepository;
import mx.com.labuena.tortillas.models.User;
import mx.com.labuena.tortillas.setup.LaBuenaApplication;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/9/16.
 */

public class ClientRegistrationIntentService extends IntentService {
    private static final String TAG = ClientRegistrationIntentService.class.getSimpleName();
    private static final String URL_FORMAT = "https://%s.appspot.com/_ah/api/";
    private static final String PROPERTIES_PATH = "configuration.properties";
    public static final String GC_PROJECT_ID_PROPERTY = "google_cloud_project_id";
    public static final String USER_DATA_EXTRA = "NewUserData";
    private static final int NEW_CLIENT_NOTIFICATION_ID = 23;

    @Inject
    PreferencesRepository sharedPreferencesRepository;

    public ClientRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);
        User user = intent.getParcelableExtra(USER_DATA_EXTRA);
        String rootUrl = getRootUrl();
        Clients.Builder builder = new Clients.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Clients clientsService = builder.build();
        try {
            clientsService.save(buildClient(user)).execute();
            sharedPreferencesRepository.save(ClientInstanceIdService.TOKEN_IN_SERVER_KEY, true);
            Log.d(TAG, "Client successfully registered.");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_add_alert_white)
                            .setContentTitle("Welcome " + user.getName())
                            .setContentText("You have been successfully registered.");
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NEW_CLIENT_NOTIFICATION_ID, mBuilder.build());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private Client buildClient(User user) {
        Client client = new Client();
        client.setName(user.getName());
        client.setEmail(user.getEmail());
        client.setFcmToken(user.getFcmToken());
        return client;
    }

    private String getRootUrl() {
        return String.format(URL_FORMAT, getProjectId());
    }

    @NonNull
    private String getProjectId() {
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

    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }
}
