package mx.com.labuena.branch.views.providers;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Random;

import mx.com.labuena.branch.R;

/**
 * Created by clerks on 8/16/16.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];


            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stock_widget);

            Random ramdom = new Random();

            remoteViews.setTextViewText(R.id.manufactureTextView, String.valueOf(ramdom.nextInt(50)));
            remoteViews.setTextViewText(R.id.externalStockTextView, String.valueOf(ramdom.nextInt(50)) + "kg");
            remoteViews.setTextViewText(R.id.ordersToDeliverTextView, String.valueOf(ramdom.nextInt(50)) + "kg");

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
