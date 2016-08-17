package mx.com.labuena.branch.views.providers;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import mx.com.labuena.branch.services.StockWidgetService;

/**
 * Created by clerks on 8/16/16.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, StockWidgetService.class));
    }
}
