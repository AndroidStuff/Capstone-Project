package mx.com.labuena.branch.presenters;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import mx.com.labuena.branch.events.EmailNotFromBranchEvent;
import mx.com.labuena.branch.events.FailureAuthenticationEvent;
import mx.com.labuena.branch.events.InvalidInputCredentialsEvent;
import mx.com.labuena.branch.events.ReplaceFragmentEvent;
import mx.com.labuena.branch.models.Action;
import mx.com.labuena.branch.models.Credentials;
import mx.com.labuena.branch.models.User;
import mx.com.labuena.branch.utils.EndpointUtils;
import mx.com.labuena.branch.views.fragments.BikersLocationFragment;
import mx.com.labuena.services.branches.Branches;
import mx.com.labuena.services.branches.model.EmailValidationResponse;

import static mx.com.labuena.branch.utils.EndpointUtils.getApplicationName;


/**
 * Created by clerks on 8/6/16.
 */

public class LoginPresenter extends BasePresenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private static final String BRANCH_VALIDATION_HANDLER = "BranchValidationHandler";
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
                FirebaseUser user = (FirebaseUser) params[0];
                validateUser(user);
            }
        };
    }

    private void validateUser(final FirebaseUser user) {
        HandlerThread handlerThread = new HandlerThread(BRANCH_VALIDATION_HANDLER);
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (emailFromBranch(user.getEmail())) {
                    navigateToBikersFragment(user);
                } else {
                    eventBus.post(new EmailNotFromBranchEvent());
                }
            }
        });
    }

    private boolean emailFromBranch(String email) {
        String rootUrl = EndpointUtils.getRootUrl(application);
        Branches.Builder builder = new Branches.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(application))
                .setRootUrl(rootUrl);

        Branches branchesService = builder.build();
        try {

            EmailValidationResponse branchEmailValidationResponse = branchesService.emailFromBranch(email).execute();
            return branchEmailValidationResponse.getValidEmail();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return false;
    }

    public void navigateToBikersFragment(FirebaseUser firebaseUser) {
        nextActionAfterAuthenticate = null;
        Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
        User user = new User(firebaseUser.getEmail(), firebaseUser.getDisplayName());
        Log.d(TAG, "User:" + user);
        eventBus.post(new ReplaceFragmentEvent(BikersLocationFragment.newInstance(user), false));
    }
}
