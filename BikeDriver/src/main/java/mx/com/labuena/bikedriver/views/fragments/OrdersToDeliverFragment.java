package mx.com.labuena.bikedriver.views.fragments;

import android.os.Bundle;
import android.view.View;

import mx.com.labuena.bikedriver.R;
import mx.com.labuena.bikedriver.models.BikeDriver;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class OrdersToDeliverFragment extends BaseFragment {

    public static final String BIKE_DRIVER_KEY = "bikeDriver";

    @Override
    protected int getLayoutId() {
        return R.layout.orders_to_deliver_fragment;
    }

    @Override
    protected void initView(View rootView) {
        View toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

    public static OrdersToDeliverFragment newInstance(BikeDriver bikeDriver) {

        Bundle args = new Bundle();
        args.putParcelable(BIKE_DRIVER_KEY, bikeDriver);
        OrdersToDeliverFragment fragment = new OrdersToDeliverFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
