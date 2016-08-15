package mx.com.labuena.bikedriver.views.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mx.com.labuena.bikedriver.R;
import mx.com.labuena.bikedriver.assemblers.OrderConverter;
import mx.com.labuena.bikedriver.data.BikeDriverContracts;
import mx.com.labuena.bikedriver.events.AddressReceivedEvent;
import mx.com.labuena.bikedriver.models.BikeDriver;
import mx.com.labuena.bikedriver.models.Coordinate;
import mx.com.labuena.bikedriver.models.Order;
import mx.com.labuena.bikedriver.services.FetchAddressIntentService;
import mx.com.labuena.bikedriver.services.OrderUpdateIntentService;
import mx.com.labuena.bikedriver.setup.LaBuenaModules;

import static mx.com.labuena.bikedriver.R.id.map;
import static mx.com.labuena.bikedriver.R.id.orderDeliveredActionButton;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class OrdersToDeliverFragment extends BaseFragment implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String BIKE_DRIVER_KEY = "bikeDriver";
    private static final int URL_LOADER = 23;
    private static final String TAG = OrdersToDeliverFragment.class.getSimpleName();
    public static final String TORTILLAS_AMOUNT_FORMAT_KG = "%d kg";
    private GoogleMap googleMap;
    private List<Order> orders = new ArrayList<>();
    Map<String, Order> markerOrderMap = new HashMap<>();
    private TextView tortillasAmountTextView;
    private TextView clientNameTextView;
    private TextView clientAddressTextView;

    @Inject
    EventBus eventBus;

    private Order currentOrder;
    private SupportMapFragment mapsFragment;

    private Marker currentMarker;

    @Override
    protected int getLayoutId() {
        return R.layout.orders_to_deliver_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        clientNameTextView = (TextView) rootView.findViewById(R.id.nameTextView);
        clientAddressTextView = (TextView) rootView.findViewById(R.id.addressTextView);
        tortillasAmountTextView = (TextView) rootView.findViewById(R.id.amountTextView);

        FloatingActionButton deliverOrderButton = (FloatingActionButton) rootView.findViewById(orderDeliveredActionButton);
        deliverOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMarker != null)
                    currentMarker.remove();

                if (currentOrder != null)
                    deleteOrder(currentOrder);
            }
        });

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarApp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mapsFragment.getMapAsync(this);
    }

    public static OrdersToDeliverFragment newInstance(BikeDriver bikeDriver) {

        Bundle args = new Bundle();
        args.putParcelable(BIKE_DRIVER_KEY, bikeDriver);
        OrdersToDeliverFragment fragment = new OrdersToDeliverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!eventBus.isRegistered(this))
            eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (eventBus.isRegistered(this))
            eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressReceivedEvent(AddressReceivedEvent event) {
        clientAddressTextView.setText(event.getAddress());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        String[] projection = new String[]{
                BikeDriverContracts.OrderEntry.ID,
                BikeDriverContracts.OrderEntry.CLIENT_EMAIL_COLUMN,
                BikeDriverContracts.OrderEntry.CLIENT_NAME_COLUMN,
                BikeDriverContracts.OrderEntry.LATITUDE_COLUMN,
                BikeDriverContracts.OrderEntry.LONGITUDE_COLUMN,
                BikeDriverContracts.OrderEntry.QUANTITY_COLUMN
        };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                BikeDriverContracts.OrderEntry.CONTENT_URI
                , projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.setNotificationUri(getActivity().getContentResolver(), BikeDriverContracts.OrderEntry.CONTENT_URI);
        cursor.moveToFirst();
        orders.clear();
        while (!cursor.isAfterLast()) {
            orders.add(OrderConverter.toModel(cursor));
            cursor.moveToNext();
        }

        displayMarkers();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        orders.clear();
        markerOrderMap.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markerOrderMap.containsKey(marker.getId())) {
            currentMarker = marker;
            currentOrder = markerOrderMap.get(marker.getId());
            displayOrder(currentOrder);
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    private void displayMarkers() {
        markerOrderMap.clear();
        googleMap.clear();

        Order lastOrder = null;
        Coordinate coordinates = null;

        for (Order order :
                orders) {
            lastOrder = order;
            coordinates = order.getCoordinates();
            String snipped = String.format(TORTILLAS_AMOUNT_FORMAT_KG, order.getQuantity());
            currentMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .title(order.getClientName())
                    .snippet(snipped));
            currentMarker.showInfoWindow();
            markerOrderMap.put(currentMarker.getId(), order);
        }

        if (lastOrder != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            displayOrder(lastOrder);

        }
    }

    private void displayOrder(Order order) {
        String snipped = String.format(TORTILLAS_AMOUNT_FORMAT_KG, order.getQuantity());
        clientNameTextView.setText(order.getClientName());
        tortillasAmountTextView.setText(snipped);
        Coordinate coordinate = order.getCoordinates();
        Location location = new Location(StringUtils.EMPTY);
        location.setLatitude(coordinate.getLatitude());
        location.setLongitude(coordinate.getLongitude());
        startIntentService(location);
    }

    private void deleteOrder(Order order) {
        Intent intent = new Intent(getActivity(), OrderUpdateIntentService.class);
        intent.putExtra(OrderUpdateIntentService.ORDER_DATA_EXTRA, order);
        getActivity().startService(intent);
    }

    private void startIntentService(Location location) {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, location);
        getActivity().startService(intent);
    }
}
