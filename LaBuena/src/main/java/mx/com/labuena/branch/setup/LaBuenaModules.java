package mx.com.labuena.branch.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.branch.services.BikerRegistrationService;
import mx.com.labuena.branch.services.FetchAddressIntentService;
import mx.com.labuena.branch.services.FindBikersService;
import mx.com.labuena.branch.views.activities.HomeActivity;
import mx.com.labuena.branch.views.fragments.BikerRegistrationFragment;
import mx.com.labuena.branch.views.fragments.BikersFragment;
import mx.com.labuena.branch.views.fragments.BikersLocationFragment;
import mx.com.labuena.branch.views.fragments.ForgotPasswordFragment;
import mx.com.labuena.branch.views.fragments.LoginFragment;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity homeActivity);

    void inject(LoginFragment loginFragment);

    void inject(BikersLocationFragment bikersLocationFragment);

    void inject(BikersFragment bikersFragment);

    void inject(FetchAddressIntentService fetchAddressIntentService);

    void inject(FindBikersService findBikersService);

    void inject(BikerRegistrationService bikerRegistrationService);

    void inject(BikerRegistrationFragment bikerRegistrationFragment);

    void inject(ForgotPasswordFragment forgotPasswordFragment);
}
