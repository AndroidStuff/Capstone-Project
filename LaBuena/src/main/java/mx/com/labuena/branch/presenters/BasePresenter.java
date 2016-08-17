package mx.com.labuena.branch.presenters;

import android.app.Application;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by clerks on 8/6/16.
 */

public class BasePresenter {
    protected final EventBus eventBus;
    protected final Application application;

    public BasePresenter(Application application, EventBus eventBus) {
        this.application = application;
        this.eventBus = eventBus;
    }
}
