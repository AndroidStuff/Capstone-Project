package mx.com.labuena.branch.presenters;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.branch.events.FailureAuthenticationEvent;
import mx.com.labuena.branch.events.InvalidInputCredentialsEvent;
import mx.com.labuena.branch.events.ReplaceFragmentEvent;
import mx.com.labuena.branch.models.Action;
import mx.com.labuena.branch.models.Credentials;
import mx.com.labuena.branch.models.User;
import mx.com.labuena.branch.views.fragments.BikersFragment;


/**
 * Created by clerks on 8/6/16.
 */

public class LoginPresenter extends BasePresenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private final FirebaseAuth.AuthStateListener authListener;

    private Action nextActionAfterAuthenticate;

    @Inject
    public LoginPresenter(Application application, final EventBus eventBus) {
        super(application, eventBus);
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
                navigateToOrdersDeliveryFragment((FirebaseUser) params[0]);
            }
        };
    }

    public void navigateToOrdersDeliveryFragment(FirebaseUser firebaseUser) {
        nextActionAfterAuthenticate = null;
        Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
        User user = new User(firebaseUser.getEmail(), firebaseUser.getDisplayName());
        Log.d(TAG, "User:" + user);
        eventBus.post(new ReplaceFragmentEvent(BikersFragment.newInstance(user), false));
    }
}
