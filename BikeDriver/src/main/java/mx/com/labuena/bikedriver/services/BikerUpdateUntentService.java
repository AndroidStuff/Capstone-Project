package mx.com.labuena.bikedriver.services;

import android.content.Intent;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class BikerUpdateUntentService extends EndpointConsumerBaseService {
    private static final String TAG = BikerUpdateUntentService.class.getSimpleName();
    public static final String BIKER_DATA_EXTRA = "BikerExtraData";

    public BikerUpdateUntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
