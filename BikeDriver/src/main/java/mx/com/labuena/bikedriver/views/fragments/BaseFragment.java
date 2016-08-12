package mx.com.labuena.bikedriver.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.com.labuena.bikedriver.setup.LaBuenaApplication;
import mx.com.labuena.bikedriver.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/12/2016.
 */

public abstract class BaseFragment  extends Fragment {
    private int layoutId;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        loadFragmentArguments();
        initFragment(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(getLayoutId(), container, false);
        initView(rootView);
        return rootView;
    }

    protected void initView(View rootView) {

    }

    protected void initFragment(Bundle savedInstanceState) {
    }

    protected void loadFragmentArguments() {

    }

    protected void injectDependencies(LaBuenaModules modules) {

    }

    protected abstract int getLayoutId();

    private void injectDependencies() {
        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(getActivity()
                .getApplicationContext());
        injectDependencies(modules);
    }
}