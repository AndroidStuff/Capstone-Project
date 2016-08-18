package mx.com.labuena.branch.views.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.AddressReceivedEvent;
import mx.com.labuena.branch.events.BikersReceivedEvent;
import mx.com.labuena.branch.models.User;
import mx.com.labuena.branch.presenters.BikersPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.services.bikers.model.Biker;
import mx.com.labuena.services.bikers.model.BikerLocation;
import mx.com.labuena.services.bikers.model.Coordinates;

/**
 * Created by moracl6 on 8/17/2016.
 */

public class BikersFragment extends BaseFragment implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    public static final String DATA_USER_KEY = "DataUser";

    @Inject
    EventBus eventBus;

    @Inject
    BikersPresenter bikersPresenter;

    Map<String, Biker> markerBikerMap = new HashMap<>();

    private GoogleMap googleMap;
    private TextView bikerNameTextView;
    private TextView bikerPhoneTextView;
    private TextView bikerStockTextView;
    private TextView bikerAddressTextView;
    private SupportMapFragment mapsFragment;
    private User user;


    @Override
    protected int getLayoutId() {
        return R.layout.bikers_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    protected void initView(View rootView) {
        bikerNameTextView = (TextView) rootView.findViewById(R.id.nameTextView);
        bikerPhoneTextView = (TextView) rootView.findViewById(R.id.phoneTextView);
        bikerStockTextView = (TextView) rootView.findViewById(R.id.stockTextView);
        bikerAddressTextView = (TextView) rootView.findViewById(R.id.addressTextView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarApp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapsFragment.getMapAsync(this);
    }

    public static BikersFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(DATA_USER_KEY, user);
        BikersFragment fragment = new BikersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadFragmentArguments() {
        if (getArguments() != null) {
            this.user = getArguments().getParcelable(DATA_USER_KEY);
        }
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
        bikerAddressTextView.setText(event.getAddress());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBikersReceivedEvent(BikersReceivedEvent event) {
        displayBikers(event.getBikers());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markerBikerMap.containsKey(marker.getId())) {
            Biker biker = markerBikerMap.get(marker.getId());
            displayBiker(biker);
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        bikersPresenter.getBikers();
    }

    private void displayBikers(List<Biker> bikers) {
        markerBikerMap.clear();
        googleMap.clear();

        Coordinates coordinates = null;
        Biker lastBiker = null;

        for (Biker biker :
                bikers) {
            BikerLocation lastLocation = biker.getBikerLocation();
            coordinates = lastLocation.getCoordinates();
            String snipped = String.format(getString(R.string.map_snipped_format), biker.getLastStock());
            Marker currentMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .title(biker.getName())
                    .snippet(snipped));
            currentMarker.showInfoWindow();
            markerBikerMap.put(currentMarker.getId(), biker);
        }

        if (lastBiker != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            displayBiker(lastBiker);

        }

    }

    private void displayBiker(Biker biker) {

        bikerNameTextView.setText(biker.getName());
        bikerPhoneTextView.setText(biker.getPhone());
        bikerStockTextView.setText(R.string.tortillas_to_deliver_format);
        BikerLocation bikerLocation = biker.getBikerLocation();
        Coordinates coordinates = bikerLocation.getCoordinates();
        Location location = new Location(StringUtils.EMPTY);
        location.setLatitude(coordinates.getLatitude());
        location.setLongitude(coordinates.getLongitude());

        bikersPresenter.findLocation(location);
    }
}
