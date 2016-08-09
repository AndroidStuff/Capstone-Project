package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.models.User;

/**
 * Created by clerks on 8/7/16.
 */

public class TortillasRequestorFragment extends BaseFragment {

    public static final String LOGIN_USER_KEY = "LoginUser";

    public static final int DEFAULT_CONSUME = 3;

    public static final int MAX_AMOUNT = 40;
    public static final int MIN_AMOUNT = 1;


    private User user;

    private int tortillasAmount = DEFAULT_CONSUME;

    private TextView tortillasAmontTextview;

    @Override
    protected int getLayoutId() {
        return R.layout.tortillas_requestor_fragment;
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

        addEventsToControls(rootView);
    }

    private void addEventsToControls(View rootView) {
        Button buttonMoreTortillas = (Button) rootView.findViewById(R.id.increaseAmountButton);
        buttonMoreTortillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tortillasAmount <= MAX_AMOUNT) {
                    ++tortillasAmount;
                    displayTortillasAmount();
                }
            }
        });

        Button buttonLessTortillas = (Button) rootView.findViewById(R.id.decreaseAmountButton);
        buttonLessTortillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tortillasAmount > MIN_AMOUNT) {
                    --tortillasAmount;
                    displayTortillasAmount();
                }
            }
        });
    }

    private void displayTortillasAmount() {
        tortillasAmontTextview.setText(String.format("%d", tortillasAmount));
    }
}
