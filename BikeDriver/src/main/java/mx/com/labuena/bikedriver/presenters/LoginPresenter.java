package mx.com.labuena.bikedriver.presenters;

import android.app.Activity;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import mx.com.labuena.bikedriver.events.FailureAuthenticationEvent;
import mx.com.labuena.bikedriver.events.InvalidInputCredentialsEvent;
import mx.com.labuena.bikedriver.events.ReplaceFragmentEvent;
import mx.com.labuena.bikedriver.models.Action;
import mx.com.labuena.bikedriver.models.BikeDriver;
import mx.com.labuena.bikedriver.models.Credentials;
import mx.com.labuena.bikedriver.models.PreferencesRepository;
import mx.com.labuena.bikedriver.services.BikerInstanceIdService;
import mx.com.labuena.bikedriver.services.BikerLocationUpdateService;
import mx.com.labuena.bikedriver.services.BikerUpdateIntentService;
import mx.com.labuena.bikedriver.views.fragments.OrdersToDeliverFragment;


/**
 * Created by clerks on 8/6/16.
 */

public class LoginPresenter extends BasePresenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    public static final int LOCATION_UPDATE_JOB_ID = 11;
    private final FirebaseAuth.AuthStateListener authListener;

    PreferencesRepository preferencesRepository;

    private Action nextActionAfterAuthenticate;

    @Inject
    public LoginPresenter(Application application, final EventBus eventBus, PreferencesRepository preferencesRepository) {
        super(application, eventBus);
        this.preferencesRepository = preferencesRepository;

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (isUserAuthenticated(firebaseUser)) {
                    if (nextActionAfterAuthenticate != null)
                        nextActionAfterAuthenticate.execute(firebaseUser);

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public FirebaseAuth.AuthStateListener getAuthListener() {
        return authListener;
    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        return user != null;
    }

    public void authenticate(final Activity activity, final Credentials credentials, FirebaseAuth firebaseAuth) {
        setNextActionAfterAuthenticate();

        if (credentials.isValid()) {
            firebaseAuth.signInWithEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                eventBus.post(new FailureAuthenticationEvent(credentials));
                            }


                        }
                    });
        } else {
            eventBus.post(new InvalidInputCredentialsEvent(credentials));
        }
    }

    private void setNextActionAfterAuthenticate() {
        nextActionAfterAuthenticate = new Action() {
            @Override
            public void execute(Object... params) {
                startLocationUpdatesService();
                navigateToOrdersDeliveryFragment((FirebaseUser) params[0]);
            }
        };
    }

    private void navigateToOrdersDeliveryFragment(FirebaseUser firebaseUser) {
        nextActionAfterAuthenticate = null;
        Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
        BikeDriver bikeDriver = new BikeDriver(firebaseUser.getEmail(), firebaseUser.getDisplayName());
        Log.d(TAG, "Biker:" + bikeDriver);
        updateBikeDriver(bikeDriver);
        eventBus.post(new ReplaceFragmentEvent(OrdersToDeliverFragment.newInstance(bikeDriver), false));
    }

    private void updateBikeDriver(BikeDriver bikeDriver) {
        boolean tokenInServer = preferencesRepository.read(BikerInstanceIdService.TOKEN_SAVED_IN_SERVER_KEY, false);
        if (!tokenInServer) {
            String token = preferencesRepository.read(BikerInstanceIdService.REGISTRATION_TOKEN_KEY, StringUtils.EMPTY);
            bikeDriver.setFcmToken(token);
            Intent intent = new Intent(application, BikerUpdateIntentService.class);
            intent.putExtra(BikerUpdateIntentService.BIKER_DATA_EXTRA, bikeDriver);
            application.startService(intent);
        }
    }

    private void startLocationUpdatesService() {
        ComponentName serviceComponent = new ComponentName(application, BikerLocationUpdateService.class);
        JobInfo.Builder builder = new JobInfo.Builder(LOCATION_UPDATE_JOB_ID, serviceComponent);
        builder.setPeriodic(TimeUnit.MINUTES.toMillis(BikerLocationUpdateService.LOCATION_UPDATE_PERIOD));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setRequiresCharging(false);
        JobScheduler jobScheduler = (JobScheduler) application.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
