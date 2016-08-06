package mx.com.labuena.tortillas.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.tortillas.HomeActivity;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity scanCardFragment);
}