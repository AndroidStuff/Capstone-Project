package mx.com.labuena.tortillas.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.ResetPasswordFailureEvent;
import mx.com.labuena.tortillas.events.ResetPasswordSuccessfulEvent;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class ForgotPasswordPresenter extends BasePresenter {
    private static final String TAG = ForgotPasswordPresenter.class.getSimpleName();

    @Inject
    public ForgotPasswordPresenter(EventBus eventBus) {
        super(eventBus);
    }

    public void resetPassword(final String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            eventBus.post(new ResetPasswordSuccessfulEvent(email));
                        } else {
                            Log.e(TAG, task.getException().getMessage(), task.getException());
                            eventBus.post(new ResetPasswordFailureEvent(email));
                        }
                    }
                });
    }
}
