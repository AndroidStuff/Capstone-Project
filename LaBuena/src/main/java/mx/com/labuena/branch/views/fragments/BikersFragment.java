package mx.com.labuena.branch.views.fragments;

import android.support.v4.app.Fragment;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.models.User;

/**
 * Created by moracl6 on 8/17/2016.
 */

public class BikersFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
    }

    public static Fragment newInstance(User user) {
        return null;
    }
}
