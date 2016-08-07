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

import mx.com.labuena.tortillas.events.InvalidInputCredentialsEvent;
import mx.com.labuena.tortillas.models.Credentials;

/**
 * Created by clerks on 8/6/16.
 */

public class LoginPresenter extends BasePresenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private final FirebaseAuth.AuthStateListener mAuthListener;


    @Inject
    public LoginPresenter(EventBus eventBus) {
        super(eventBus);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (isUserAuthenticated(user)) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        return user != null;
    }

    public void authenticate(final Activity activity, Credentials credentials, FirebaseAuth firebaseAuth) {
        if (credentials.isValid()) {
            firebaseAuth.signInWithEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {

                            }


                        }
                    });
        } else {
            eventBus.post(new InvalidInputCredentialsEvent(credentials));
        }
    }

    public void authenticateUsingGmail(Credentials credentials) {

    }

    public void authenticateUsingFacebook(Credentials credentials) {

    }
}
