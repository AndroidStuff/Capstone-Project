package mx.com.labuena.tortillas.presenters;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by clerks on 8/6/16.
 */

public class BasePresenter {
    private final EventBus eventBus;

    public BasePresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
