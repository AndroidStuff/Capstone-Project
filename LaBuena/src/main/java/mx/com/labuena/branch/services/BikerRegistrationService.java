package mx.com.labuena.branch.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.setup.LaBuenaApplication;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.branch.utils.EndpointUtils;
import mx.com.labuena.services.bikers.Bikers;

import static mx.com.labuena.branch.utils.EndpointUtils.getApplicationName;

/**
 * Created by moracl6 on 8/18/2016.
 */

public class BikerRegistrationService extends IntentService {
    private static final String TAG = BikerRegistrationService.class.getSimpleName();
    public static final String BIKER_DATA_EXTRA = "BikerData";
    private static final int NEW_BIKER_NOTIFICATION_ID = 17;

    public BikerRegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);
        Biker biker = intent.getParcelableExtra(BIKER_DATA_EXTRA);
        String rootUrl = EndpointUtils.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {
            bikersService.save(buildBiker(biker)).execute();
            Log.d(TAG, "Biker successfully registered.");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_add_alert_white)
                            .setContentTitle(getString(R.string.biker_added_notification_title))
                            .setContentText(biker.getName()+getString(R.string.biker_added_notification_content));
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NEW_BIKER_NOTIFICATION_ID, mBuilder.build());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    private mx.com.labuena.services.bikers.model.Biker buildBiker(Biker biker) {
        mx.com.labuena.services.bikers.model.Biker bikerTo = new mx.com.labuena.services.bikers.model.Biker();
        bikerTo.setEmail(biker.getEmail());
        bikerTo.setName(biker.getName());
        bikerTo.setPhone(biker.getPhone());
        return bikerTo;
    }
}