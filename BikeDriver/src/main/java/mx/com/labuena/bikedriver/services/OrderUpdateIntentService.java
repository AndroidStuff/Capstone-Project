package mx.com.labuena.bikedriver.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import mx.com.labuena.bikedriver.data.BikeDriverContracts;
import mx.com.labuena.bikedriver.models.Order;
import mx.com.labuena.bikedriver.utils.EndpointUtil;
import mx.com.labuena.services.bikers.Bikers;

import static mx.com.labuena.bikedriver.utils.EndpointUtil.getApplicationName;

/**
 * Created by clerks on 8/14/16.
 */

public class OrderUpdateIntentService extends IntentService {
    private static final String TAG = OrderUpdateIntentService.class.getSimpleName();
    public static final String ORDER_DATA_EXTRA = "OrderExtraData";

    public OrderUpdateIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Order order = intent.getParcelableExtra(ORDER_DATA_EXTRA);
        String rootUrl = EndpointUtil.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {
            bikersService.updateOrder(buildOrderTo(order)).execute();
            Log.d(TAG, "Order updated successfully.");
            deleteLocalOrder(order);
            Log.d(TAG, "Local Order deleted.");

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void deleteLocalOrder(Order order) {
        String where = BikeDriverContracts
                .OrderEntry.ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(order.getOrderId())};
        getApplicationContext().getContentResolver().delete(BikeDriverContracts
                .OrderEntry.CONTENT_URI, where, selectionArgs);
    }

    private mx.com.labuena.services.bikers.model.Order buildOrderTo(Order order) {
        mx.com.labuena.services.bikers.model.Order orderTo =
                new mx.com.labuena.services.bikers.model.Order();
        orderTo.setOrderId(order.getOrderId());
        return orderTo;
    }
}
