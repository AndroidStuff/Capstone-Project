package mx.com.labuena.tortillas.presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.FailureAuthenticationEvent;
import mx.com.labuena.tortillas.events.InvalidInputCredentialsEvent;
import mx.com.labuena.tortillas.events.ReplaceFragmentEvent;
import mx.com.labuena.tortillas.models.Credentials;
import mx.com.labuena.tortillas.models.User;
import mx.com.labuena.tortillas.views.fragments.TortillasRequestorFragment;

/**
 * Created by moracl6 on 8/9/2016.
 */

public class ClientRegistrationPresenter extends BasePresenter {
    private static final String TAG = ClientRegistrationPresenter.class.getSimpleName();
    private final FirebaseAuth.AuthStateListener mAuthListener;

    @Inject
    public ClientRegistrationPresenter(final EventBus eventBus) {
        super(eventBus);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (isUserAuthenticated(firebaseUser)) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
                    Log.d(TAG, "User:" + user);
                    eventBus.post(new ReplaceFragmentEvent(TortillasRequestorFragment.newInstance(user), false));
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void createUser(final Activity activity, FirebaseAuth mAuth, final Credentials credentials) {
        if (credentials.isValid()) {
            mAuth.createUserWithEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.e(TAG, "We couldn't create the user");
                                eventBus.post(new FailureAuthenticationEvent(credentials));
                            }
                        }
                    });
        } else {
            eventBus.post(new InvalidInputCredentialsEvent(credentials));
        }
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        return user != null;
    }
}
