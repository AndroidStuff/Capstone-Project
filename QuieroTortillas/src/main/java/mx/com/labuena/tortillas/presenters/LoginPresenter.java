package mx.com.labuena.tortillas.presenters;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.FailureAuthenticationEvent;
import mx.com.labuena.tortillas.events.InvalidInputCredentialsEvent;
import mx.com.labuena.tortillas.events.ReplaceFragmentEvent;
import mx.com.labuena.tortillas.models.Credentials;
import mx.com.labuena.tortillas.models.PreferencesRepository;
import mx.com.labuena.tortillas.models.User;
import mx.com.labuena.tortillas.services.ClientInstanceIdService;
import mx.com.labuena.tortillas.services.ClientRegistrationIntentService;
import mx.com.labuena.tortillas.views.fragments.ClientRegistrationFragment;
import mx.com.labuena.tortillas.views.fragments.ForgotPasswordFragment;
import mx.com.labuena.tortillas.views.fragments.TortillasRequestorFragment;

/**
 * Created by clerks on 8/6/16.
 */

public class LoginPresenter extends BasePresenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private final FirebaseAuth.AuthStateListener mAuthListener;
    private final GoogleApiClient.OnConnectionFailedListener googleClientListener;
    private final Application application;

    PreferencesRepository preferencesRepository;


    @Inject
    public LoginPresenter(final EventBus eventBus, PreferencesRepository preferencesRepository, Application application) {
        super(eventBus);

        this.preferencesRepository = preferencesRepository;
        this.application = application;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (isUserAuthenticated(firebaseUser)) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
                    Log.d(TAG, "User:" + user);
                    registerUser(user);
                    eventBus.post(new ReplaceFragmentEvent(TortillasRequestorFragment.newInstance(user), false));
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        googleClientListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        };
    }

    private void registerUser(User user) {
        boolean tokenInServer = preferencesRepository.read(ClientInstanceIdService.TOKEN_IN_SERVER_KEY, false);
        if (!tokenInServer) {
            String token = preferencesRepository.read(ClientInstanceIdService.REGISTRATION_TOKEN_KEY, StringUtils.EMPTY);
            user.setFcmToken(token);
            Intent intent = new Intent(application, ClientRegistrationIntentService.class);
            intent.putExtra(ClientRegistrationIntentService.USER_DATA_EXTRA, user);
            application.startService(intent);
        }
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public GoogleApiClient.OnConnectionFailedListener getGoogleClientListener() {
        return googleClientListener;
    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        return user != null;
    }

    public void authenticate(final Activity activity, final Credentials credentials, FirebaseAuth firebaseAuth) {
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

    public void authenticateUsingGmail(Credentials credentials) {

    }

    public void authenticateUsingFacebook(Credentials credentials) {

    }

    public void firebaseAuthWithGoogle(final Activity activity, FirebaseAuth firebaseAuth, GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                        }

                    }
                });
    }

    public void navigateToForgotPassword() {
        eventBus.post(new ReplaceFragmentEvent(new ForgotPasswordFragment(), true));
    }

    public void navigateToRegisterUser() {
        eventBus.post(new ReplaceFragmentEvent(new ClientRegistrationFragment(), true));
    }
}
