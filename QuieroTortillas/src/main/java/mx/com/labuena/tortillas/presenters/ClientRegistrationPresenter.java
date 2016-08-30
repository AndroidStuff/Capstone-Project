package mx.com.labuena.tortillas.presenters;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.InvalidInputClientEvent;
import mx.com.labuena.tortillas.events.RegistrationFailureEvent;
import mx.com.labuena.tortillas.events.ReplaceFragmentEvent;
import mx.com.labuena.tortillas.events.UserAlreadyRegisterEvent;
import mx.com.labuena.tortillas.models.Action;
import mx.com.labuena.tortillas.models.Client;
import mx.com.labuena.tortillas.models.PreferencesRepository;
import mx.com.labuena.tortillas.models.User;
import mx.com.labuena.tortillas.services.ClientInstanceIdService;
import mx.com.labuena.tortillas.services.ClientRegistrationIntentService;
import mx.com.labuena.tortillas.views.fragments.TortillasRequestorFragment;

/**
 * Created by moracl6 on 8/9/2016.
 */

public class ClientRegistrationPresenter extends BasePresenter {
    private static final String TAG = ClientRegistrationPresenter.class.getSimpleName();
    private final FirebaseAuth.AuthStateListener mAuthListener;
    private final PreferencesRepository preferencesRepository;
    private final Application application;
    private String clientName;
    private Action action;

    @Inject
    public ClientRegistrationPresenter(final EventBus eventBus,
                                       PreferencesRepository preferencesRepository, Application application) {
        super(eventBus);
        this.preferencesRepository = preferencesRepository;
        this.application = application;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (isUserAuthenticated(firebaseUser)) {
                    if (action != null)
                        action.execute(firebaseUser);
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void updateUserName(final FirebaseUser firebaseUser, final Client client) {
        action = new Action() {
            @Override
            public void execute(Object... params) {
                User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(),
                        clientName, firebaseUser.getPhotoUrl());
                registerUser(user);
                eventBus.post(new ReplaceFragmentEvent(TortillasRequestorFragment.newInstance(user), false));
            }
        };

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(clientName)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            eventBus.post(new RegistrationFailureEvent(client));
                            return;

                        }
                    }
                });
    }

    public void createUser(final Activity activity, FirebaseAuth mAuth, final Client client) {
        this.clientName = client.getName();

        action = new Action() {
            @Override
            public void execute(Object... params) {
                updateUserName((FirebaseUser) params[0], client);
            }
        };

        if (client.isValid()) {
            mAuth.createUserWithEmailAndPassword(client.getEmail(), client.getPassword())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.e(TAG, "We couldn't create the user");

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    eventBus.post(new UserAlreadyRegisterEvent());
                                    return;
                                }

                                eventBus.post(new RegistrationFailureEvent(client));
                            }
                        }
                    });
        } else {
            eventBus.post(new InvalidInputClientEvent(client));
        }
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        return user != null;
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
}
