package mx.com.labuena.branch.views.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.InvalidBikerEvent;
import mx.com.labuena.branch.events.RegistrationFailureEvent;
import mx.com.labuena.branch.events.BikerAlreadyRegisterEvent;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.presenters.ClientRegistrationPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.services.clients.model.Client;


/**
 * Created by clerks on 8/7/16.
 */

public class BikerRegistrationFragment extends BaseFragment {
    @Inject
    EventBus eventBus;

    @Inject
    ClientRegistrationPresenter clientRegistrationPresenter;

    private FirebaseAuth firebaseAuth;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private ProgressBar progressBar;
    private EditText userNameEditText;

    @Override
    protected int getLayoutId() {
        return R.layout.biker_registration_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void initView(View rootView) {
        userNameEditText = (EditText) rootView.findViewById(R.id.nameEditText);
        userEmailEditText = (EditText) rootView.findViewById(R.id.emailEditText);
        userPasswordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadControlEvents(rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(clientRegistrationPresenter.getmAuthListener());
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
        super.onStop();
        if (clientRegistrationPresenter.getmAuthListener() != null) {
            firebaseAuth.removeAuthStateListener(clientRegistrationPresenter.getmAuthListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvalidInputClientEvent(InvalidBikerEvent event) {
        progressBar.setVisibility(View.GONE);
        Biker biker = event.getBiker();
        if (TextUtils.isEmpty(biker.getName())) {
            userNameEditText.setError(getString(R.string.user_name_required));
            userNameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(biker.getEmail())) {
            userEmailEditText.setError(getString(R.string.email_address_required));
            userEmailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(biker.getPassword())) {
            userPasswordEditText.setError(getString(R.string.password_required));
            userPasswordEditText.requestFocus();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegistrationFailureEvent(RegistrationFailureEvent event) {
        progressBar.setVisibility(View.GONE);
        Biker biker = event.getBiker();
        if (biker.getPassword().length() < 6) {
            userEmailEditText.setError(getString(R.string.minimum_password));
            userEmailEditText.requestFocus();
        } else {
            Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserAlreadyRegisterEvent(BikerAlreadyRegisterEvent event) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.duplicate_biker), Toast.LENGTH_LONG).show();
    }

    private void loadControlEvents(View rootView) {
        View newUserButton = rootView.findViewById(R.id.signUpButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Biker biker = getUserInput();
                clientRegistrationPresenter.createBiker(getActivity(), firebaseAuth, biker);
            }
        });
    }

    public Biker getUserInput() {
        String name = userNameEditText.getText().toString();
        return new Biker(name, userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString(), StringUtils.EMPTY);
    }
}
