package mx.com.labuena.bikedriver.views.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import mx.com.labuena.bikedriver.R;
import mx.com.labuena.bikedriver.assemblers.OrderConverter;
import mx.com.labuena.bikedriver.data.BikeDriverContracts;
import mx.com.labuena.bikedriver.models.BikeDriver;
import mx.com.labuena.bikedriver.models.Coordinate;
import mx.com.labuena.bikedriver.models.Order;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class OrdersToDeliverFragment extends BaseFragment implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String BIKE_DRIVER_KEY = "bikeDriver";
    private static final int URL_LOADER = 23;
    private static final String TAG = OrdersToDeliverFragment.class.getSimpleName();
    private GoogleMap googleMap;
    private List<Order> orders = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.orders_to_deliver_fragment;
    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        View toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
        while (!cursor.isAfterLast()) {
            orders.add(OrderConverter.toModel(cursor));
            cursor.moveToNext();
        }

        displayMarkers();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    private void displayMarkers() {
        Coordinate coordinates = null;

        for (Order order :
                orders) {
            coordinates = order.getCoordinates();
            String snipped = String.format("%d kg", order.getQuantity());
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .title(order.getClientName())
                    .snippet(snipped));

            Log.d(TAG, "Displaying marker for order " + order);
        }

        if (coordinates != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }
}
