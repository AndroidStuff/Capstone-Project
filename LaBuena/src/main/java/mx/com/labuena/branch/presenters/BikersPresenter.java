package mx.com.labuena.branch.presenters;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.branch.services.FetchAddressIntentService;
import mx.com.labuena.branch.services.FindBikersService;

/**
 * Created by clerks on 8/17/16.
 */

public class BikersPresenter extends BasePresenter {
    private static final String FIND_BIKERS_HANDLER = "FindBikers";

    @Inject
    public BikersPresenter(Application application, EventBus eventBus) {
        super(application, eventBus);
    }


    public void findLocation(Location location) {
        Intent intent = new Intent(application, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, location);
        application.startService(intent);
    }

    public void getBikers() {
        HandlerThread handlerThread = new HandlerThread(FIND_BIKERS_HANDLER);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(application, FindBikersService.class);
                application.startService(intent);
            }
        });
    }
}
