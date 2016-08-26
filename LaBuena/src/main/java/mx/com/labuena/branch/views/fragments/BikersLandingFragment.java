package mx.com.labuena.branch.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.BikersReceivedEvent;
import mx.com.labuena.branch.events.UpdateBikersRequiredEvent;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.models.User;
import mx.com.labuena.branch.presenters.BikersPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.branch.views.adapters.ViewPagerAdapter;

/**
 * Created by clerks on 8/22/16.
 */

public class BikersLandingFragment extends BaseFragment {
    public static final String DATA_USER_KEY = "DataUser";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private User user;

    @Inject
    EventBus eventBus;

    @Inject
    BikersPresenter bikersPresenter;

    private ProgressBar loadingProgressBar;

    private ViewPagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.bikers_landing_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    public static BikersLandingFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(DATA_USER_KEY, user);
        BikersLandingFragment fragment = new BikersLandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadFragmentArguments() {
        if (getArguments() != null) {
            this.user = getArguments().getParcelable(DATA_USER_KEY);
        }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        viewPager = (ViewPager) getView().findViewById(R.id.homeViewPager);
        tabLayout = (TabLayout) getView().findViewById(R.id.branchTabLayout);

        loadingProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

        bikersPresenter.getBikers();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBikersReceivedEvent(BikersReceivedEvent event) {
        loadingProgressBar.setVisibility(View.GONE);
        setupViewPager(event.getBikers());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateBikersRequiredEvent(UpdateBikersRequiredEvent event) {
        eventBus.removeStickyEvent(event);
        loadingProgressBar.setVisibility(View.VISIBLE);
        bikersPresenter.getBikers();
    }

    private void setupViewPager(ArrayList<Biker> bikers) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(BikersLocationFragment.newInstance(bikers), getString(R.string.biker_tab_location));
        adapter.addFragment(BikersFragment.newInstance(bikers), getString(R.string.biker_tab_management));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
