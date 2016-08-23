package mx.com.labuena.branch.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.BikersReceivedEvent;
import mx.com.labuena.branch.presenters.BikersPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.branch.views.adapters.BikersAdapter;
import mx.com.labuena.services.bikers.model.Biker;

/**
 * Created by clerks on 8/22/16.
 */

public class BikersFragment extends BaseFragment {
    @Inject
    EventBus eventBus;

    @Inject
    BikersPresenter bikersPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.bikers_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    protected void initView(View rootView) {

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarApp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bikersPresenter.getBikers();
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
    public void onBikersReceivedEvent(BikersReceivedEvent event) {
        displayBikers(event.getBikers());
    }

    private void displayBikers(List<Biker> bikers) {
        BikersAdapter adapter = new BikersAdapter(getActivity(), bikers);

        final RecyclerView addonsRecyclerView = (RecyclerView) getView().findViewById(R.id.bikersRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);

        addonsRecyclerView.setAdapter(adapter);
        addonsRecyclerView.setLayoutManager(linearLayoutManager);
        addonsRecyclerView.setHasFixedSize(true);
    }
}
