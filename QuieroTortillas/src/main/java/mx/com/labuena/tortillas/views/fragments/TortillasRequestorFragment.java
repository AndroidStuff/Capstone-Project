package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.TortillasRequestChangedEvent;
import mx.com.labuena.tortillas.models.TortillasRequest;
import mx.com.labuena.tortillas.models.User;
import mx.com.labuena.tortillas.presenters.TortillasRequestorPresenter;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/7/16.
 */

public class TortillasRequestorFragment extends BaseFragment {

    public static final String LOGIN_USER_KEY = "LoginUser";
    private static final long REP_DELAY = 100;

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
                .resize(100, 100)
                .centerCrop()
                .into(userPhoto);

        TextView nameTextView = (TextView) rootView.findViewById(R.id.welcomeTextView);
        nameTextView.setText(String.format("Welcome %s", user.getName()));

        tortillasAmontTextview = (TextView) rootView.findViewById(R.id.amountTextview);
        displayTortillasAmount();

        addEventsToControls(rootView);
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
    public void onTortillasRequestChangedEvent(TortillasRequestChangedEvent event) {
        tortillasRequest = event.getTortillasRequest();
        displayTortillasAmount();
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
        tortillasAmontTextview.setText(String.format("%d", tortillasRequest.getAmount()));
    }
}
