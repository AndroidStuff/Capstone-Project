package mx.com.labuena.bikedriver.setup;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.BuildConfig;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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

    @Provides
    @Singleton
    public final Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    public EventBus providesEventBus() {
        return EventBus.builder().throwSubscriberException(BuildConfig.DEBUG)
                .logSubscriberExceptions(true).throwSubscriberException(true).build();
    }

    @Provides
    @Singleton
    public final SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
