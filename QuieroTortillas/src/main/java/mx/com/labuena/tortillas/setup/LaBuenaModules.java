package mx.com.labuena.tortillas.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.tortillas.views.activities.HomeActivity;
import mx.com.labuena.tortillas.views.fragments.LoginFragment;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity homeActivity);
    void inject(LoginFragment loginFragment);
}
