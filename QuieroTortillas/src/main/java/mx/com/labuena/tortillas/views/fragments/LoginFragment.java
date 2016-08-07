package mx.com.labuena.tortillas.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.SuccessfulAuthenticationEvent;
import mx.com.labuena.tortillas.models.Credentials;
import mx.com.labuena.tortillas.presenters.LoginPresenter;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/6/16.
 */

public class LoginFragment extends BaseFragment {
    @Inject
    LoginPresenter loginPresenter;

    @Inject
    EventBus eventBus;

    private FirebaseAuth mAuth;

    private EditText userEmailEditText;

    private EditText userPasswordEditText;

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
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        userEmailEditText = (EditText) getView().findViewById(R.id.emailEditText);
        userPasswordEditText = (EditText) getView().findViewById(R.id.passwordEditText);
        loadControlEvents(rootView);
    }

    private void loadControlEvents(View rootView) {
        AppCompatButton loginButton = (AppCompatButton) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials credentials = getUserInputCredentials();
                loginPresenter.authenticate(credentials);
            }
        });

        AppCompatButton loginWithGmailButton = (AppCompatButton) rootView.findViewById(R.id.gmailSigninButton);
        loginWithGmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials credentials = getUserInputCredentials();
                loginPresenter.authenticateUsingGmail(credentials);
            }
        });

        AppCompatButton loginWithFacebookButton = (AppCompatButton) rootView.findViewById(R.id.facebookSigninButton);
        loginWithFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials credentials = getUserInputCredentials();
                loginPresenter.authenticateUsingFacebook(credentials);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(loginPresenter.getmAuthListener());
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
        if (loginPresenter.getmAuthListener() != null) {
            mAuth.removeAuthStateListener(loginPresenter.getmAuthListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessfulAuthenticationEvent(SuccessfulAuthenticationEvent event) {

    }

    public Credentials getUserInputCredentials() {
        return new Credentials(userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString());
    }
}
