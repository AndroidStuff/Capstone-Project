package mx.com.labuena.branch.views.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.EmailNotFromBranchEvent;
import mx.com.labuena.branch.events.FailureAuthenticationEvent;
import mx.com.labuena.branch.events.InvalidInputCredentialsEvent;
import mx.com.labuena.branch.models.Credentials;
import mx.com.labuena.branch.presenters.LoginPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;
import mx.com.labuena.branch.utils.InputMethodManagerExtensor;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class LoginFragment extends BaseFragment {
    @Inject
    LoginPresenter loginPresenter;

    @Inject
    EventBus eventBus;

    private FirebaseAuth firebaseAuth;

    private EditText userEmailEditText;

    private EditText userPasswordEditText;
    private ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
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
        userEmailEditText = (EditText) rootView.findViewById(R.id.emailEditText);
        userPasswordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadControlEvents(rootView);
    }

    private void loadControlEvents(View rootView) {
        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManagerExtensor.hideKeyBoard(getActivity());
                progressBar.setVisibility(View.VISIBLE);
                Credentials credentials = getUserInputCredentials();
                loginPresenter.authenticate(getActivity(), credentials, firebaseAuth);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(loginPresenter.getAuthListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!eventBus.isRegistered(this))
            eventBus.register(this);
    }

    private boolean userLogged() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        return currentUser != null;
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
        if (loginPresenter.getAuthListener() != null) {
            firebaseAuth.removeAuthStateListener(loginPresenter.getAuthListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvalidInputCredentialsEvent(InvalidInputCredentialsEvent event) {
        progressBar.setVisibility(View.GONE);
        Credentials credentials = event.getCredentials();
        if (TextUtils.isEmpty(credentials.getEmail())) {
            userEmailEditText.setError(getString(R.string.email_address_required));
            userEmailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(credentials.getPassword())) {
            userPasswordEditText.setError(getString(R.string.password_required));
            userPasswordEditText.requestFocus();
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
    public void onEmailNotFromBranchEvent(EmailNotFromBranchEvent event) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.email_not_from_branch), Toast.LENGTH_LONG).show();
    }

    public Credentials getUserInputCredentials() {
        return new Credentials(userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString());
    }
}