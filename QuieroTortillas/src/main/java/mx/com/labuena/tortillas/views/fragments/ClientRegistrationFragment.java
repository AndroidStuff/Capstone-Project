package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.FailureAuthenticationEvent;
import mx.com.labuena.tortillas.events.InvalidInputCredentialsEvent;
import mx.com.labuena.tortillas.events.UserAlreadyRegisterEvent;
import mx.com.labuena.tortillas.models.Credentials;
import mx.com.labuena.tortillas.presenters.ClientRegistrationPresenter;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

import static mx.com.labuena.tortillas.R.id.nameEditText;

/**
 * Created by clerks on 8/7/16.
 */

public class ClientRegistrationFragment extends BaseFragment {
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
        return R.layout.client_registration_fragment;
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
        userNameEditText = (EditText) rootView.findViewById(nameEditText);
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
    public void onInvalidInputCredentialsEvent(InvalidInputCredentialsEvent event) {
        progressBar.setVisibility(View.GONE);
        Credentials credenttials = event.getCredentials();
        if (TextUtils.isEmpty(credenttials.getEmail())) {
            Toast.makeText(getActivity(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(credenttials.getPassword())) {
            Toast.makeText(getActivity(), "Enter password!", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFailureAuthenticationEvent(FailureAuthenticationEvent event) {
        progressBar.setVisibility(View.GONE);
        Credentials credentials = event.getCredentials();
        if (credentials.getPassword().length() < 6) {
            userEmailEditText.setError(getString(R.string.minimum_password));
        } else {
            Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserAlreadyRegisterEvent(UserAlreadyRegisterEvent event) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.duplicate_user), Toast.LENGTH_LONG).show();
    }

    private void loadControlEvents(View rootView) {
        View newUserButton = rootView.findViewById(R.id.signUpButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Credentials credentials = getUserInputCredentials();
                String name = userNameEditText.getText().toString();
                clientRegistrationPresenter.createUser(getActivity(), firebaseAuth, credentials, name);
            }
        });
    }

    public Credentials getUserInputCredentials() {
        return new Credentials(userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString());
    }
}
