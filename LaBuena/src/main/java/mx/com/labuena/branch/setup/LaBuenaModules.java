package mx.com.labuena.branch.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.branch.services.FetchAddressIntentService;
import mx.com.labuena.branch.services.FindBikersService;
import mx.com.labuena.branch.views.activities.HomeActivity;
import mx.com.labuena.branch.views.fragments.BikersFragment;
import mx.com.labuena.branch.views.fragments.LoginFragment;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity homeActivity);

    void inject(LoginFragment loginFragment);

    void inject(BikersFragment bikersFragment);

    void inject(FetchAddressIntentService fetchAddressIntentService);

    void inject(FindBikersService findBikersService);
}
