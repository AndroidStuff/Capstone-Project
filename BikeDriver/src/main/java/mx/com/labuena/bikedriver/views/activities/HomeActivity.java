package mx.com.labuena.bikedriver.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.bikedriver.R;
import mx.com.labuena.bikedriver.events.ReplaceFragmentEvent;
import mx.com.labuena.bikedriver.setup.LaBuenaApplication;
import mx.com.labuena.bikedriver.setup.LaBuenaModules;
import mx.com.labuena.bikedriver.utils.GooglePlayServicesUtil;
import mx.com.labuena.bikedriver.views.fragments.LoginFragment;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class HomeActivity extends AppCompatActivity {
    public static final String NAVIGATE_TO_PENDING_ORDERS_EXTRA = "navigateToPendingOrders";
    @Inject
    EventBus eventBus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(this
                .getApplicationContext());
        modules.inject(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LoginFragment()).commit();
        }

        if (!GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            GooglePlayServicesUtil.requestGooglePlayServices(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!eventBus.isRegistered(this))
            eventBus.register(this);

        if (!GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            GooglePlayServicesUtil.requestGooglePlayServices(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (eventBus.isRegistered(this))
            eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReplaceFragmentEvent(ReplaceFragmentEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction
                .replace(R.id.fragment_container, event.getFragment());

        if (event.isRequiredToAddInBackStack())
            transaction.addToBackStack(null);

        transaction.commit();
    }
}
