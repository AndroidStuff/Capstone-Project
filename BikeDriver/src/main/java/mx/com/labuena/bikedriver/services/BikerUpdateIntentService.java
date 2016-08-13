package mx.com.labuena.bikedriver.services;

import android.content.Intent;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class BikerUpdateIntentService extends EndpointConsumerBaseService {
    private static final String TAG = BikerUpdateIntentService.class.getSimpleName();
    public static final String BIKER_DATA_EXTRA = "BikerExtraData";

    public BikerUpdateIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
