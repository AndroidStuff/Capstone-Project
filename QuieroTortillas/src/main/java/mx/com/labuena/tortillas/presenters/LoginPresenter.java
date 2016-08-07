package mx.com.labuena.tortillas.presenters;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by clerks on 8/6/16.
 */

public class LoginPresenter extends BasePresenter {
    @Inject
    public LoginPresenter(EventBus eventBus) {
        super(eventBus);
    }
}
