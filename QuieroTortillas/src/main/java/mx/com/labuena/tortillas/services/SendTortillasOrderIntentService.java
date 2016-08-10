package mx.com.labuena.tortillas.services;

import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import javax.inject.Inject;

import mx.com.labuena.services.clients.Clients;
import mx.com.labuena.services.clients.model.Coordinates;
import mx.com.labuena.services.clients.model.Order;
import mx.com.labuena.tortillas.models.DeviceLocation;
import mx.com.labuena.tortillas.models.PreferencesRepository;
import mx.com.labuena.tortillas.models.TortillasRequest;
import mx.com.labuena.tortillas.setup.LaBuenaApplication;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/9/2016.
 */

public class SendTortillasOrderIntentService extends EndpointConsumerBaseService {

    private static final String TAG = SendTortillasOrderIntentService.class.getSimpleName();

    public static final String ORDER_DATA_EXTRA = "OrderDataExtra";

    @Inject
    PreferencesRepository preferencesRepository;

    public SendTortillasOrderIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        TortillasRequest tortillasRequest = intent.getParcelableExtra(ORDER_DATA_EXTRA);

        String rootUrl = getRootUrl();
        Clients.Builder builder = new Clients.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Clients clientsService = builder.build();
        try {
            clientsService.requestTortillas(buildOrder(tortillasRequest)).execute();
            Log.d(TAG, "The tortillas order has been send.");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    private Order buildOrder(TortillasRequest tortillasRequest) {
        Order order = new Order();
        order.setClientEmail(tortillasRequest.getUser().getEmail());
        order.setCoordinates(buildCoordinates(tortillasRequest.getDeviceLocation()));
        order.setQuantity(tortillasRequest.getAmount());
        return order;
    }

    private Coordinates buildCoordinates(DeviceLocation deviceLocation) {
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(deviceLocation.getLatitude());
        coordinates.setLongitude(deviceLocation.getLongitude());
        return coordinates;
    }
}
