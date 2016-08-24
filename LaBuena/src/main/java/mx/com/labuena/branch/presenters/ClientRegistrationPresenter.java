package mx.com.labuena.branch.presenters;

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

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.branch.events.BikerAlreadyRegisterEvent;
import mx.com.labuena.branch.events.InvalidBikerEvent;
import mx.com.labuena.branch.events.RegistrationFailureEvent;
import mx.com.labuena.branch.models.Action;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.services.BikerRegistrationService;

/**
 * Created by moracl6 on 8/9/2016.
 */

public class ClientRegistrationPresenter extends BasePresenter {
    private static final String TAG = ClientRegistrationPresenter.class.getSimpleName();
    private final FirebaseAuth.AuthStateListener mAuthListener;
    private Biker biker;
    private Action nextAction;

    @Inject
    public ClientRegistrationPresenter(final EventBus eventBus, Application application) {
        super(application, eventBus);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (isUserAuthenticated(firebaseUser)) {
                    if (nextAction != null)
                        nextAction.execute(firebaseUser);
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void registerBikerInServer(Biker biker) {
        Intent intent = new Intent(application, BikerRegistrationService.class);
        intent.putExtra(BikerRegistrationService.BIKER_DATA_EXTRA, biker);
        application.startService(intent);
    }

    public void createBiker(final Activity activity, FirebaseAuth mAuth, final Biker biker) {
        this.biker = biker;
        nextAction = new Action() {
            @Override
            public void execute(Object... params) {
                registerBikerInServer(biker);
            }
        };

        if (biker.isValid()) {
            mAuth.createUserWithEmailAndPassword(biker.getEmail(), biker.getPassword())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createBikerWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.e(TAG, "We couldn't create the biker");

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    eventBus.post(new BikerAlreadyRegisterEvent());
                                    return;
                                }

                                eventBus.post(new RegistrationFailureEvent(biker));
                            }
                        }
                    });
        } else {
            eventBus.post(new InvalidBikerEvent(biker));
        }
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        return user != null;
    }
}
