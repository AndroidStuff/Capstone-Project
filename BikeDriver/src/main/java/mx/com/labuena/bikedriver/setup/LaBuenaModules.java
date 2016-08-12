package mx.com.labuena.bikedriver.setup;

import javax.inject.Singleton;

import dagger.Component;
import mx.com.labuena.bikedriver.views.activities.HomeActivity;

/**
 * Created by clerks on 8/6/16.
 */
@Singleton
@Component(modules = {LaBuenaApplicationModules.class})
public interface LaBuenaModules {
    void inject(HomeActivity homeActivity);
}
