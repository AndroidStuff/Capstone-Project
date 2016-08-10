package mx.com.labuena.tortillas.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.tortillas.services.ClientInstanceIdService;
import mx.com.labuena.tortillas.services.FetchAddressIntentService;
import mx.com.labuena.tortillas.services.ClientRegistrationIntentService;
import mx.com.labuena.tortillas.services.SendTortillasOrderIntentService;
import mx.com.labuena.tortillas.views.activities.HomeActivity;
import mx.com.labuena.tortillas.views.fragments.ClientRegistrationFragment;
import mx.com.labuena.tortillas.views.fragments.LoginFragment;
import mx.com.labuena.tortillas.views.fragments.TortillasRequestorFragment;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity homeActivity);
    void inject(LoginFragment loginFragment);

    void inject(ClientInstanceIdService clientInstanceIdService);

    void inject(TortillasRequestorFragment tortillasRequestorFragment);

    void inject(FetchAddressIntentService fetchAddressIntentService);

    void inject(SendTortillasOrderIntentService sendTortillasOrderIntentService);

    void inject(ClientRegistrationFragment clientRegistrationFragment);

    void inject(ClientRegistrationIntentService clientRegistrationIntentService);
}
