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
import mx.com.labuena.branch.events.BikerAlreadyRegisterEvent;
import mx.com.labuena.branch.events.InvalidBikerEvent;
import mx.com.labuena.branch.events.ProcessingBikerRegistrationEvent;
import mx.com.labuena.branch.events.RegistrationFailureEvent;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.presenters.BikerRegistrationPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;


/**
 * Created by clerks on 8/7/16.
 */

public class BikerRegistrationFragment extends BaseFragment {
    @Inject
    EventBus eventBus;

    @Inject
    BikerRegistrationPresenter bikerRegistrationPresenter;

    private FirebaseAuth firebaseAuth;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private ProgressBar progressBar;
    private EditText userNameEditText;
    private EditText userPhoneEditText;

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
        userPhoneEditText = (EditText) rootView.findViewById(R.id.phoneEditText);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadControlEvents(rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(bikerRegistrationPresenter.getmAuthListener());
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
        if (bikerRegistrationPresenter.getmAuthListener() != null) {
            firebaseAuth.removeAuthStateListener(bikerRegistrationPresenter.getmAuthListener());
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

        if (TextUtils.isEmpty(biker.getPhone())) {
            userPhoneEditText.setError(getString(R.string.user_phone_required));
            userPhoneEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(biker.getPassword())) {
            userPasswordEditText.setError(getString(R.string.password_required));
            userPasswordEditText.requestFocus();
            return;
        }

        if (!biker.isValidPhone()) {
            userPhoneEditText.setError(getString(R.string.invalid_phone_number));
            userPhoneEditText.requestFocus();
            return;
        }

        if (!biker.isValidEmail()) {
            userEmailEditText.setError(getString(R.string.invalid_email));
            userEmailEditText.requestFocus();
            return;
        }

        if (!biker.isValidPassword()) {
            userPasswordEditText.setError(getString(R.string.invalid_password));
            userPasswordEditText.requestFocus();
            return;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingBikerRegistrationEvent(ProcessingBikerRegistrationEvent event) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.processing_biker_registration), Toast.LENGTH_LONG).show();
        clearBikerInputControls();
    }

    private void clearBikerInputControls() {
        userEmailEditText.setText(StringUtils.EMPTY);
        userPasswordEditText.setText(StringUtils.EMPTY);
        userNameEditText.setText(StringUtils.EMPTY);
        userPhoneEditText.setText(StringUtils.EMPTY);
    }

    private void loadControlEvents(View rootView) {
        View newUserButton = rootView.findViewById(R.id.signUpButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Biker biker = getUserInput();
                bikerRegistrationPresenter.createBiker(getActivity(), firebaseAuth, biker);
            }
        });
    }

    public Biker getUserInput() {
        return new Biker(userNameEditText.getText().toString(), userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString(), userPhoneEditText.getText().toString());
    }
}
