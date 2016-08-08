package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.view.View;
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

    User user;

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
                .into(userPhoto);

        TextView nameTextView = (TextView)rootView.findViewById(R.id.welcomeTextView);
        nameTextView.setText(String.format("Welcome %s", user.getName()));
    }
}
