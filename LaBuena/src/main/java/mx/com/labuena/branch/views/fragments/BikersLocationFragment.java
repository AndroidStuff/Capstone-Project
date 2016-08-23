package mx.com.labuena.branch.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.models.Coordinates;
import mx.com.labuena.branch.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/17/2016.
 */

public class BikersLocationFragment extends BaseFragment implements OnMapReadyCallback {

    public static final String DATA_USER_KEY = "Bikers";

    private GoogleMap googleMap;
    private SupportMapFragment mapsFragment;
    private ArrayList<Biker> bikers;


    @Override
    protected int getLayoutId() {
        return R.layout.bikers_location_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapsFragment.getMapAsync(this);
    }

    public static BikersLocationFragment newInstance(ArrayList<Biker> bikers) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_USER_KEY, bikers);
        BikersLocationFragment fragment = new BikersLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadFragmentArguments() {
        if (getArguments() != null) {
            this.bikers = getArguments().getParcelableArrayList(DATA_USER_KEY);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        displayBikers();
    }

    private void displayBikers() {
        googleMap.clear();

        Coordinates coordinates = null;
        Biker lastBiker = null;

        for (Biker biker :
                bikers) {
            lastBiker = biker;
            coordinates = biker.getLastLocation();
            String snipped = String.format(getString(R.string.map_snipped_format), biker.getLastStock());
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .title(biker.getName())
                    .snippet(snipped));
        }

        if (lastBiker != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()))
                    .zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

    }
}
