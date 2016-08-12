package mx.com.labuena.bikedriver.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import mx.com.labuena.bikedriver.models.PreferencesRepository;
import mx.com.labuena.bikedriver.setup.LaBuenaApplication;
import mx.com.labuena.bikedriver.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/8/2016.
 */

public class BikerInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = BikerInstanceIdService.class.getSimpleName();
    public static final String REGISTRATION_TOKEN_KEY = "RegistrationToken";
    public static final String TOKEN_IN_SERVER_KEY = "RegistrationTokenSavedInServer";

    @Inject
    PreferencesRepository preferencesRepository;

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh");
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        saveRegistrationToken(refreshedToken);
    }

    private void saveRegistrationToken(String refreshedToken) {
        preferencesRepository.save(REGISTRATION_TOKEN_KEY, refreshedToken);
        preferencesRepository.save(TOKEN_IN_SERVER_KEY, false);
    }
}
