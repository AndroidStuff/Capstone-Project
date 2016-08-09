package mx.com.labuena.tortillas.views.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.AddressReceivedEvent;
import mx.com.labuena.tortillas.events.TortillasRequestChangedEvent;
import mx.com.labuena.tortillas.models.DeviceLocation;
import mx.com.labuena.tortillas.models.TortillasRequest;
import mx.com.labuena.tortillas.models.User;
import mx.com.labuena.tortillas.presenters.TortillasRequestorPresenter;
import mx.com.labuena.tortillas.services.FetchAddressIntentService;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/7/16.
 */

public class TortillasRequestorFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String LOGIN_USER_KEY = "LoginUser";
    public static final int USER_IMAGE_HEIGHT = 100;
    private static final long REP_DELAY = 100;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 23;
    public static final int USER_IMAGE_WIDTH = 100;

    private Handler repeatUpdateHandler = new Handler();

    private boolean autoIncrement = false;

    private boolean autoDecrement = false;

    @Inject
    TortillasRequestorPresenter tortillasRequestorPresenter;

    @Inject
    EventBus eventBus;

    private User user;

    private TextView tortillasAmontTextview;

    private TortillasRequest tortillasRequest;

    private GoogleApiClient googleApiClient;

    private Location lastLocation;

    private TextView locationDeliveryTextView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        if (lastLocation != null) {
            tortillasRequest.setDeviceLocation(new DeviceLocation(lastLocation.getLatitude(),
                    lastLocation.getLongitude()));

            startIntentService(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class UpdateAmount implements Runnable {
        public void run() {
            if (autoIncrement) {
                tortillasRequestorPresenter.increaseOrderAmount(tortillasRequest);
                repeatUpdateHandler.postDelayed(new UpdateAmount(), REP_DELAY);
                return;
            }

            if (autoDecrement) {
                tortillasRequestorPresenter.decreaseOrderAmount(tortillasRequest);
                repeatUpdateHandler.postDelayed(new UpdateAmount(), REP_DELAY);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tortillas_requestor_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public static TortillasRequestorFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(LOGIN_USER_KEY, user);
        TortillasRequestorFragment fragment = new TortillasRequestorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadFragmentArguments() {
        if (getArguments() != null) {
            user = getArguments().getParcelable(LOGIN_USER_KEY);
            tortillasRequest = new TortillasRequest(user);
        }
    }

    @Override
    protected void initView(View rootView) {
        ImageView userPhoto = (ImageView) rootView.findViewById(R.id.contactImageView);
        Picasso.with(getActivity())
                .load(user.getPhotoUri())
                .resize(USER_IMAGE_WIDTH, USER_IMAGE_HEIGHT)
                .centerCrop()
                .into(userPhoto);

        TextView nameTextView = (TextView) rootView.findViewById(R.id.welcomeTextView);
        String welcomeMessage = getString(R.string.welcome_message);
        nameTextView.setText(String.format(welcomeMessage, user.getName()));

        locationDeliveryTextView = (TextView) rootView.findViewById(R.id.locationDeliveryTextView);

        tortillasAmontTextview = (TextView) rootView.findViewById(R.id.amountTextview);
        displayTortillasAmount();

        addEventsToControls(rootView);
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();

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

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTortillasRequestChangedEvent(TortillasRequestChangedEvent event) {
        tortillasRequest = event.getTortillasRequest();
        displayTortillasAmount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressReceivedEvent(AddressReceivedEvent event) {
        String deliveryMessage = getString(R.string.delivery_message);
        locationDeliveryTextView.setText(String.format(deliveryMessage, event.getAddress()));
    }


    private void addEventsToControls(View rootView) {
        Button buttonMoreTortillas = (Button) rootView.findViewById(R.id.increaseAmountButton);
        buttonMoreTortillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tortillasRequestorPresenter.increaseOrderAmount(tortillasRequest);
            }
        });

        buttonMoreTortillas.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoIncrement = true;
                        repeatUpdateHandler.post(new UpdateAmount());
                        return false;
                    }
                }
        );

        buttonMoreTortillas.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && autoIncrement) {
                    autoIncrement = false;
                }
                return false;
            }
        });

        Button buttonLessTortillas = (Button) rootView.findViewById(R.id.decreaseAmountButton);
        buttonLessTortillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tortillasRequestorPresenter.decreaseOrderAmount(tortillasRequest);
            }
        });

        buttonLessTortillas.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoDecrement = true;
                        repeatUpdateHandler.post(new UpdateAmount());
                        return false;
                    }
                }
        );

        buttonLessTortillas.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && autoDecrement) {
                    autoDecrement = false;
                }
                return false;
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                rootView.findViewById(R.id.requestTortillasActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tortillasRequestorPresenter.requestOrder(tortillasRequest);
            }
        });
    }

    private void displayTortillasAmount() {
        String amountFormat = getString(R.string.tortillas_amount_format);
        String formattedAmount = String.format(amountFormat, tortillasRequest.getAmount());
        SpannableString spannableString = new SpannableString(formattedAmount);
        int measureUnitStart = formattedAmount.length() - 3;
        spannableString.setSpan(new RelativeSizeSpan(0.2f), measureUnitStart, formattedAmount.length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), measureUnitStart, formattedAmount.length(), 0);
        tortillasAmontTextview.setText(spannableString);
    }

    private void startIntentService(Location location) {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, location);
        getActivity().startService(intent);
    }
}
