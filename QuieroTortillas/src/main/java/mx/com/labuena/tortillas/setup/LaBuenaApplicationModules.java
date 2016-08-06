package mx.com.labuena.tortillas.setup;

import android.app.Application;

import dagger.Module;

/**
 * Created by clerks on 8/6/16.
 */
@Module(
)
public class LaBuenaApplicationModules {
    private final Application application;

    public LaBuenaApplicationModules(Application application) {
        this.application = application;
    }
}
