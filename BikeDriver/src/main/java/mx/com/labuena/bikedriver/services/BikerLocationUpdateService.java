package mx.com.labuena.bikedriver.services;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.util.DateTime;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import javax.inject.Inject;

import mx.com.labuena.bikedriver.models.PreferencesRepository;
import mx.com.labuena.bikedriver.setup.LaBuenaApplication;
import mx.com.labuena.bikedriver.setup.LaBuenaModules;
import mx.com.labuena.bikedriver.utils.EndpointUtil;
import mx.com.labuena.bikedriver.utils.LocationHelper;
import mx.com.labuena.services.bikers.Bikers;
import mx.com.labuena.services.bikers.model.Biker;
import mx.com.labuena.services.bikers.model.BikerLocation;
import mx.com.labuena.services.bikers.model.Coordinates;

import static mx.com.labuena.bikedriver.utils.EndpointUtil.getApplicationName;


/**
 * Created by moracl6 on 8/15/2016.
 */

public class BikerLocationUpdateService extends JobService {
    private static final String TAG = BikerLocationUpdateService.class.getSimpleName();
    public static final int TWO_MINUTEST_DELAY_MILLIS = 120000;

    /**
     * Location update period. Value in minutes
     */
    public static final int LOCATION_UPDATE_PERIOD = 10;

    @Inject
    PreferencesRepository preferencesRepository;

    @Override
    public boolean onStartJob(JobParameters params) {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final LocationHelper locationHelper = new LocationHelper();

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (locationHelper.isBetterLocation(location))
                    locationHelper.setCurrentBestLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (ActivityCompat.checkSelfPermission(BikerLocationUpdateService.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                sendLocationUpdateToServer(locationHelper.getCurrentBestLocation());

                locationManager.removeUpdates(locationListener);
            }
        }, TWO_MINUTEST_DELAY_MILLIS);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private void sendLocationUpdateToServer(Location location) {
        String email = preferencesRepository.read(BikerUpdateIntentService.BIKER_EMAIL_KEY, StringUtils.EMPTY);
        String rootUrl = EndpointUtil.getRootUrl(this);
        Bikers.Builder builder = new Bikers.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null).setApplicationName(getApplicationName(this))
                .setRootUrl(rootUrl);

        Bikers bikersService = builder.build();
        try {

            bikersService.location(buildBiker(email, location)).execute();
            Log.d(TAG, "Biker Location update send to server.");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    private Biker buildBiker(String email, Location location) {
        Biker biker = new Biker();
        biker.setEmail(email);

        BikerLocation bikerLocation = new BikerLocation();
        bikerLocation.setReadAt(new DateTime(System.currentTimeMillis()));

        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(location.getLatitude());
        coordinates.setLongitude(location.getLongitude());
        bikerLocation.setCoordinates(coordinates);

        biker.setBikerLocation(bikerLocation);
        return biker;
    }
}
