package mx.com.labuena.bikedriver.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.List;

import mx.com.labuena.bikedriver.R;
import mx.com.labuena.bikedriver.assemblers.OrderConverter;
import mx.com.labuena.bikedriver.data.BikeDriverContracts;
import mx.com.labuena.bikedriver.utils.EndpointUtil;
import mx.com.labuena.bikedriver.views.activities.HomeActivity;
import mx.com.labuena.services.bikers.Bikers;
import mx.com.labuena.services.bikers.model.Order;
import mx.com.labuena.services.bikers.model.OrdersResponse;

import static mx.com.labuena.bikedriver.utils.EndpointUtil.getApplicationName;

/**
 * Created by moracl6 on 8/8/2016.
 */

public class BikerMessagingService extends FirebaseMessagingService {
    private static final String TAG = BikerMessagingService.class.getSimpleName();
    public static final int NOTIFICATION_ID = 10;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " +
                remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());

        String message = remoteMessage.getData().toString();
        mx.com.labuena.bikedriver.models.Order order = OrderConverter.toTransferObject(message);

        String rootUrl = EndpointUtil.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {
            OrdersResponse ordersResponse = bikersService.ordersToDeliver(order.getBikerId()).execute();
            Log.d(TAG, "New orders to deliver received.");

            insertOrders(ordersResponse.getOrders());
            sendNotification();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void insertOrders(List<Order> orders) {
        getApplicationContext().getContentResolver().delete(BikeDriverContracts
                .OrderEntry.CONTENT_URI, null, null);

        getApplicationContext().getContentResolver().bulkInsert(
                BikeDriverContracts.OrderEntry.CONTENT_URI,
                OrderConverter.toContentValues(orders));
    }

    private void sendNotification() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_add_alert_white)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.order_notitication))
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
