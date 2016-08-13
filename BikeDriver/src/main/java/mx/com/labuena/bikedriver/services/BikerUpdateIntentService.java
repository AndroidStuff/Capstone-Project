package mx.com.labuena.bikedriver.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import javax.inject.Inject;

import mx.com.labuena.bikedriver.models.BikeDriver;
import mx.com.labuena.bikedriver.models.PreferencesRepository;
import mx.com.labuena.bikedriver.setup.LaBuenaApplication;
import mx.com.labuena.bikedriver.setup.LaBuenaModules;
import mx.com.labuena.bikedriver.utils.EndpointUtil;
import mx.com.labuena.services.bikers.Bikers;
import mx.com.labuena.services.bikers.model.Biker;

import static mx.com.labuena.bikedriver.utils.EndpointUtil.getApplicationName;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class BikerUpdateIntentService extends IntentService {
    private static final String TAG = BikerUpdateIntentService.class.getSimpleName();
    public static final String BIKER_DATA_EXTRA = "BikerExtraData";

    @Inject
    PreferencesRepository sharedPreferencesRepository;

    public BikerUpdateIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        BikeDriver bikeDriver = intent.getParcelableExtra(BIKER_DATA_EXTRA);
        String rootUrl = EndpointUtil.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {
            bikersService.updateToken(buildBiker(bikeDriver)).execute();
            sharedPreferencesRepository.save(BikerInstanceIdService.TOKEN_IN_SERVER_KEY, true);
            Log.d(TAG, "Bike driver token successfully updated.");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private Biker buildBiker(BikeDriver bikeDriver) {
        Biker biker = new Biker();
        biker.setEmail(bikeDriver.getEmail());
        biker.setGcmToken(bikeDriver.getFcmToken());
        return biker;
    }


}
