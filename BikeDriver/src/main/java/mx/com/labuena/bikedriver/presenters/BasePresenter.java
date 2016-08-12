package mx.com.labuena.bikedriver.presenters;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by clerks on 8/6/16.
 */

public class BasePresenter {
    protected final EventBus eventBus;

    public BasePresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
