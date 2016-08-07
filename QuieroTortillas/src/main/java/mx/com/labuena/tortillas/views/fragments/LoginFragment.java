package mx.com.labuena.tortillas.views.fragments;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.SuccessfulAuthenticationEvent;
import mx.com.labuena.tortillas.presenters.LoginPresenter;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/6/16.
 */

public class LoginFragment extends BaseFragment {
    @Inject
    LoginPresenter loginPresenter;

    @Inject
    EventBus eventBus;

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!eventBus.isRegistered(this))
            eventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessfulAuthenticationEvent(SuccessfulAuthenticationEvent event) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (eventBus.isRegistered(this))
            eventBus.unregister(this);
    }
}
