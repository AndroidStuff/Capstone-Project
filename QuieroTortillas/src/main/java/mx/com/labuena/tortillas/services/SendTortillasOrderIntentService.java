package mx.com.labuena.tortillas.services;

import android.app.IntentService;
import android.content.Intent;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import mx.com.labuena.tortillas.models.PreferencesRepository;
import mx.com.labuena.tortillas.models.TortillasRequest;
import mx.com.labuena.tortillas.setup.LaBuenaApplication;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/9/2016.
 */

public class SendTortillasOrderIntentService extends IntentService {

    private static final String TAG = SendTortillasOrderIntentService.class.getSimpleName();

    private static final String ORDER_DATA_EXTRA = "OrderDataExtra";

    @Inject
    PreferencesRepository preferencesRepository;

    public SendTortillasOrderIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        TortillasRequest tortillasRequest = intent.getParcelableExtra(ORDER_DATA_EXTRA);
        loadFcmToken(tortillasRequest);

    }

    private void loadFcmToken(TortillasRequest tortillasRequest) {
        boolean clientTokenSaved = preferencesRepository.read(ClientInstanceIdService.IS_TOKEN_SAVED_IN_SERVER_KEY, false);
        if (!clientTokenSaved) {
            String fcmToken = preferencesRepository.read(ClientInstanceIdService.REGISTRATION_TOKEN_KEY, StringUtils.EMPTY);
            tortillasRequest.getUser().setFcmToken(fcmToken);
        }
    }
}
