package mx.com.labuena.branch.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.utils.EndpointUtils;
import mx.com.labuena.branch.views.providers.StockWidgetProvider;
import mx.com.labuena.services.branches.Branches;
import mx.com.labuena.services.branches.model.Stock;

import static mx.com.labuena.branch.utils.EndpointUtils.getApplicationName;

/**
 * Created by clerks on 8/16/16.
 */

public class StockWidgetService extends Service {
    private static final String TAG = StockWidgetService.class.getSimpleName();
    private static final String STOCK_UPDATES_HANDLER = "StockUpdates";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestStock();
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestStock() {
        HandlerThread handlerThread = new HandlerThread(STOCK_UPDATES_HANDLER);
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                Stock stock = getLatestStock();
                if (stock != null) {
                    RemoteViews remoteViews = new RemoteViews(StockWidgetService.this.getPackageName(),
                            R.layout.stock_widget);
                    int externalStock = stock.getExternalStock();
                    int ordersToDeliver = stock.getOrdersToDeliver();
                    int amountToManufacture = 0;

                    if (externalStock < ordersToDeliver)
                        amountToManufacture = ordersToDeliver - externalStock;

                    remoteViews.setTextViewText(R.id.manufactureTextView, String.valueOf(amountToManufacture));
                    remoteViews.setTextViewText(R.id.externalStockTextView, String.valueOf(externalStock) +  getString(R.string.amount_metric_unit));
                    remoteViews.setTextViewText(R.id.ordersToDeliverTextView, String.valueOf(ordersToDeliver) + getString(R.string.amount_metric_unit));
                    pushWidgetUpdate(remoteViews);
                }

                stopSelf();
            }
        });
    }

    private Stock getLatestStock() {
        Stock stock = null;
        String rootUrl = EndpointUtils.getRootUrl(this);
        Branches.Builder builder = new Branches.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);
        Branches branchesService = builder.build();
        try {
            stock = branchesService.getStock().execute();
            Log.e(TAG, "Stock retrieved");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return stock;
    }

    private void pushWidgetUpdate(RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(this, StockWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
