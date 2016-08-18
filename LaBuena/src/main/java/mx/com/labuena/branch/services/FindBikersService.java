package mx.com.labuena.branch.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import mx.com.labuena.branch.events.BikersReceivedEvent;
import mx.com.labuena.branch.setup.LaBuenaApplication;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.branch.utils.EndpointUtils;
import mx.com.labuena.services.bikers.Bikers;
import mx.com.labuena.services.bikers.model.BikersResponse;

/**
 * Created by clerks on 8/17/16.
 */

public class FindBikersService extends IntentService {
    private static final String TAG = FindBikersService.class.getSimpleName();

    @Inject
    EventBus eventBus;

    public FindBikersService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        String rootUrl = EndpointUtils.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(EndpointUtils.getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {

            BikersResponse bikersResponse = bikersService.getAll().execute();
            eventBus.post(new BikersReceivedEvent(bikersResponse.getBikers()));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}

