package mx.com.labuena.tortillas.presenters;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.TortillasRequestChangedEvent;
import mx.com.labuena.tortillas.models.TortillasRequest;

/**
 * Created by clerks on 8/9/16.
 */

public class TortillasRequestorPresenter extends BasePresenter {
    @Inject
    public TortillasRequestorPresenter(EventBus eventBus) {
        super(eventBus);
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

    }
}
