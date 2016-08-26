package mx.com.labuena.branch.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.UpdateBikersRequiredEvent;
import mx.com.labuena.branch.setup.LaBuenaApplication;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.branch.utils.EndpointUtils;
import mx.com.labuena.services.bikers.Bikers;
import mx.com.labuena.services.bikers.model.Biker;

import static mx.com.labuena.branch.utils.EndpointUtils.getApplicationName;

/**
 * Created by clerks on 8/25/16.
 */

public class BikerStockUpdateService extends IntentService {
    private static final String TAG = BikerStockUpdateService.class.getSimpleName();

    public static final String BIKER_DATA_EXTRA = "BikerExtraData";
    private static final int BIKER_UPDATED_NOTIFICATION_ID = 14;

    @Inject
    EventBus eventBus;

    public BikerStockUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        mx.com.labuena.branch.models.Biker biker = intent.getParcelableExtra(BIKER_DATA_EXTRA);
        String rootUrl = EndpointUtils.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {
            bikersService.updateStock(buildBiker(biker)).execute();
            Log.d(TAG, "Bike stock updated successfully.");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_add_alert_white)
                            .setContentTitle(getString(R.string.biker_stock_updated_notification_title))
                            .setContentText(biker.getName()+getString(R.string.biker_stock_updated_content));
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(BIKER_UPDATED_NOTIFICATION_ID, mBuilder.build());
            eventBus.postSticky(new UpdateBikersRequiredEvent());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private Biker buildBiker(mx.com.labuena.branch.models.Biker biker) {
        Biker bikerTo = new Biker();
        bikerTo.setEmail(biker.getEmail());
        biker.setLastStock(biker.getLastStock());
        return bikerTo;
    }
}
