package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.com.labuena.tortillas.setup.LaBuenaApplication;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/6/16.
 */

public abstract class BaseFragment extends Fragment {
    private int layoutId;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        loadFragmentArguments();
        initFragmentCreation(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(getLayoutId(), container, false);
    }

    protected void initFragmentCreation(Bundle savedInstanceState) {

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
