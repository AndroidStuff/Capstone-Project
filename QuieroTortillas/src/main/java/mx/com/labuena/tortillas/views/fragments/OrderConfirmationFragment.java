package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.ReplaceFragmentEvent;
import mx.com.labuena.tortillas.models.TortillasRequest;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/10/16.
 */

public class OrderConfirmationFragment extends BaseFragment {
    public static final String ORDER_REQUEST_KEY = "OrderRequest";

    private TortillasRequest orderRequest;

    @Inject
    EventBus eventBus;

    @Override
    protected int getLayoutId() {
        return R.layout.order_confirmation_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    public static OrderConfirmationFragment newInstance(TortillasRequest tortillasRequest) {

        Bundle args = new Bundle();
        args.putParcelable(ORDER_REQUEST_KEY, tortillasRequest);
        OrderConfirmationFragment fragment = new OrderConfirmationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        TextView orderMessage = (TextView) rootView.findViewById(R.id.orderMessageTextView);
        String messageFormat = getString(R.string.lbl_order_send);
        String message = String.format(messageFormat, orderRequest.getAmount(), orderRequest.getAddress());
        orderMessage.setText(message);

        loadControlEvents(rootView);
    }

    @Override
    protected void loadFragmentArguments() {
        if (getArguments() != null) {
            orderRequest = getArguments().getParcelable(ORDER_REQUEST_KEY);
        }
    }

    private void loadControlEvents(View rootView) {
        View newRequestButton = rootView.findViewById(R.id.newRequestButton);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new ReplaceFragmentEvent(TortillasRequestorFragment.newInstance(orderRequest.getUser()), false));
            }
        });
    }
}
