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
import mx.com.labuena.tortillas.events.InvalidInputClientEvent;
import mx.com.labuena.tortillas.events.RegistrationFailureEvent;
import mx.com.labuena.tortillas.events.UserAlreadyRegisterEvent;
import mx.com.labuena.tortillas.models.Client;
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
    public void onInvalidInputClientEvent(InvalidInputClientEvent event) {
        progressBar.setVisibility(View.GONE);
        Client client = event.getClient();
        if (TextUtils.isEmpty(client.getName())) {
            userNameEditText.setError(getString(R.string.user_name_required));
            userNameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(client.getEmail())) {
            userEmailEditText.setError(getString(R.string.email_address_required));
            userEmailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(client.getPassword())) {
            userPasswordEditText.setError(getString(R.string.password_required));
            userPasswordEditText.requestFocus();
            return;
        }

        if (!client.isValidEmail()) {
            userEmailEditText.setError(getString(R.string.invalid_email));
            userEmailEditText.requestFocus();
            return;
        }

        if (!client.isValidPassword()) {
            userPasswordEditText.setError(getString(R.string.invalid_password));
            userPasswordEditText.requestFocus();
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegistrationFailureEvent(RegistrationFailureEvent event) {
        progressBar.setVisibility(View.GONE);
        Client client = event.getClient();
        if (client.getPassword().length() < 6) {
            userEmailEditText.setError(getString(R.string.minimum_password));
            userEmailEditText.requestFocus();
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
                Client client = getUserInput();
                clientRegistrationPresenter.createUser(getActivity(), firebaseAuth, client);
            }
        });
    }

    public Client getUserInput() {
        String name = userNameEditText.getText().toString();
        return new Client(name, userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString());
    }
}
