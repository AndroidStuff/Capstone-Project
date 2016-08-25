package mx.com.labuena.branch.presenters;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.branch.events.ReplaceFragmentEvent;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.services.FindBikersService;
import mx.com.labuena.branch.views.fragments.BikerRegistrationFragment;
import mx.com.labuena.branch.views.fragments.UpdateBikerFragment;

/**
 * Created by clerks on 8/17/16.
 */

public class BikersPresenter extends BasePresenter {
    private static final String FIND_BIKERS_HANDLER = "FindBikers";

    @Inject
    public BikersPresenter(Application application, EventBus eventBus) {
        super(application, eventBus);
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

    public void navigateToBikerRegistration() {
        eventBus.post(new ReplaceFragmentEvent(new BikerRegistrationFragment(), true));
    }

    public void navigateToBikeUpdate(Biker biker) {
        eventBus.post(new ReplaceFragmentEvent(UpdateBikerFragment.newInstance(biker), true));
    }
}
