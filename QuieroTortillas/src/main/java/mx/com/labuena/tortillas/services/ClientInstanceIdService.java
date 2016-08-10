package mx.com.labuena.tortillas.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import mx.com.labuena.tortillas.models.PreferencesRepository;
import mx.com.labuena.tortillas.setup.LaBuenaApplication;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/8/2016.
 */

public class ClientInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = ClientInstanceIdService.class.getSimpleName();
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
