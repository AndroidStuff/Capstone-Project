package mx.com.labuena.branch.presenters;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.branch.events.ProcessingBikerUpdateEvent;
import mx.com.labuena.branch.events.ResetPasswordFailureEvent;
import mx.com.labuena.branch.events.ResetPasswordSuccessfulEvent;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.services.BikerStockUpdateService;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class UpdateBikerPresenter extends BasePresenter {
    private static final String TAG = UpdateBikerPresenter.class.getSimpleName();

    @Inject
    public UpdateBikerPresenter(Application application, EventBus eventBus) {
        super(application, eventBus);
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

    public void updateStock(Biker biker) {
        Intent intent = new Intent(application, BikerStockUpdateService.class);
        intent.putExtra(BikerStockUpdateService.BIKER_DATA_EXTRA, biker);
        application.startService(intent);
        eventBus.post(new ProcessingBikerUpdateEvent());
    }
}
