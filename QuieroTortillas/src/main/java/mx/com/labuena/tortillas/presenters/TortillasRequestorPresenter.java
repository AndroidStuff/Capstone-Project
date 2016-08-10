package mx.com.labuena.tortillas.presenters;

import android.app.Application;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.TortillasRequestChangedEvent;
import mx.com.labuena.tortillas.models.TortillasRequest;
import mx.com.labuena.tortillas.services.SendTortillasOrderIntentService;

/**
 * Created by clerks on 8/9/16.
 */

public class TortillasRequestorPresenter extends BasePresenter {
    private final Application application;

    @Inject
    public TortillasRequestorPresenter(EventBus eventBus, Application application) {
        super(eventBus);
        this.application = application;
    }

    public void increaseOrderAmount(TortillasRequest tortillasRequest) {
        if (tortillasRequest.isAmountUnderMaxLimit()) {
            tortillasRequest.increaseAmount();
            eventBus.post(new TortillasRequestChangedEvent(tortillasRequest));
        }
    }

    public void decreaseOrderAmount(TortillasRequest tortillasRequest) {
        if (tortillasRequest.isAmountUnderMinLimit()) {
            tortillasRequest.decreaseAmount();
            eventBus.post(new TortillasRequestChangedEvent(tortillasRequest));
        }
    }

    public void requestOrder(TortillasRequest tortillasRequest) {
        Intent intent = new Intent(application, SendTortillasOrderIntentService.class);
        intent.putExtra(SendTortillasOrderIntentService.ORDER_DATA_EXTRA, tortillasRequest);
        application.startService(intent);
    }
}
