package mx.com.labuena.tortillas.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.tortillas.R;
import mx.com.labuena.tortillas.events.FailureAuthenticationEvent;
import mx.com.labuena.tortillas.events.InvalidInputCredentialsEvent;
import mx.com.labuena.tortillas.events.ReplaceFragmentEvent;
import mx.com.labuena.tortillas.events.SuccessfulAuthenticationEvent;
import mx.com.labuena.tortillas.models.Credentials;
import mx.com.labuena.tortillas.presenters.LoginPresenter;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by clerks on 8/6/16.
 */

public class LoginFragment extends BaseFragment {
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 23;
    @Inject
    LoginPresenter loginPresenter;

    @Inject
    EventBus eventBus;

    private FirebaseAuth firebaseAuth;

    private EditText userEmailEditText;

    private EditText userPasswordEditText;
    private ProgressBar progressBar;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;

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

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), loginPresenter.getGoogleClientListener())
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
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
                progressBar.setVisibility(View.VISIBLE);
                Credentials credentials = getUserInputCredentials();
                loginPresenter.authenticate(getActivity(), credentials, firebaseAuth);
            }
        });

        SignInButton loginWithGmailButton = (SignInButton) rootView.findViewById(R.id.gmailSigninButton);
        loginWithGmailButton.setSize(SignInButton.SIZE_STANDARD);
        loginWithGmailButton.setScopes(googleSignInOptions.getScopeArray());
        loginWithGmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            }
        });

        Button loginWithFacebookButton = (Button) rootView.findViewById(R.id.facebookSigninButton);
        loginWithFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials credentials = getUserInputCredentials();
                loginPresenter.authenticateUsingFacebook(credentials);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                loginPresenter.firebaseAuthWithGoogle(getActivity(), firebaseAuth, account);
            } else {

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(loginPresenter.getmAuthListener());
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
            firebaseAuth.removeAuthStateListener(loginPresenter.getmAuthListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessfulAuthenticationEvent(SuccessfulAuthenticationEvent event) {
        eventBus.postSticky(new ReplaceFragmentEvent(new TortillasRequestorFragment(), false));
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

    public Credentials getUserInputCredentials() {
        return new Credentials(userEmailEditText.getText().toString(),
                userPasswordEditText.getText().toString());
    }
}
