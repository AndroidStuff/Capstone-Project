package mx.com.labuena.bikedriver.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.bikedriver.services.BikerInstanceIdService;
import mx.com.labuena.bikedriver.services.BikerUpdateIntentService;
import mx.com.labuena.bikedriver.views.activities.HomeActivity;
import mx.com.labuena.bikedriver.views.fragments.LoginFragment;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity homeActivity);

    void inject(BikerInstanceIdService bikerInstanceIdService);

    void inject(LoginFragment loginFragment);

    void inject(BikerUpdateIntentService bikerUpdateIntentService);
}
