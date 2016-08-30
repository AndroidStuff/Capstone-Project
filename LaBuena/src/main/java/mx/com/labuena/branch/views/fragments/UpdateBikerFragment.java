package mx.com.labuena.branch.views.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.events.ProcessingBikerUpdateEvent;
import mx.com.labuena.branch.events.ResetPasswordFailureEvent;
import mx.com.labuena.branch.events.ResetPasswordSuccessfulEvent;
import mx.com.labuena.branch.models.Biker;
import mx.com.labuena.branch.presenters.UpdateBikerPresenter;
import mx.com.labuena.branch.setup.LaBuenaModules;

import static mx.com.labuena.branch.R.id.progressBar;


/**
 * Created by moracl6 on 8/8/2016.
 */

public class UpdateBikerFragment extends BaseFragment {
    public static final String BIKERS_DATA_KEY = "BikersData";
    @Inject
    EventBus eventBus;

    @Inject
    UpdateBikerPresenter updateBikerPresenter;

    private EditText userStockEditText;

    private ProgressBar executingTaskProgressBar;

    private TextInputLayout imputEmailLayout;

    private Biker biker;

    @Override
    protected int getLayoutId() {
        return R.layout.update_biker_fragment;
    }

    @Override
    protected void injectDependencies(LaBuenaModules modules) {
        modules.inject(this);
    }

    public static UpdateBikerFragment newInstance(Biker biker) {
        Bundle args = new Bundle();
        args.putParcelable(BIKERS_DATA_KEY, biker);
        UpdateBikerFragment fragment = new UpdateBikerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadFragmentArguments() {
        if (getArguments() != null) {
            this.biker = getArguments().getParcelable(BIKERS_DATA_KEY);
        }
    }

    @Override
    protected void initView(View rootView) {
        userStockEditText = (EditText) rootView.findViewById(R.id.stockEditText);
        TextView forgotPasswordMessage = (TextView) rootView.findViewById(R.id.forgotPasswordMessageText);
        String message = String.format(getString(R.string.forgot_password_msg),
                biker.getEmail(), biker.getName());
        forgotPasswordMessage.setText(message);
        forgotPasswordMessage.setContentDescription(message);

        executingTaskProgressBar = (ProgressBar) rootView.findViewById(progressBar);
        imputEmailLayout = (TextInputLayout) rootView.findViewById(R.id.inputEmailAddress);
        loadControlEvents(rootView);
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
    public void onResetPasswordSuccessfulEvent(ResetPasswordSuccessfulEvent event) {
        executingTaskProgressBar.setVisibility(View.GONE);
        String message = String.format(getString(R.string.reset_password_email_sent), event.getEmail());
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResetPasswordFailureEvent(ResetPasswordFailureEvent event) {
        executingTaskProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.error_resetting_password), Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingBikerUpdateEvent(ProcessingBikerUpdateEvent event) {
        executingTaskProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getString(R.string.processing_biker_update), Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadControlEvents(View rootView) {
        View resetPasswordButton = rootView.findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executingTaskProgressBar.setVisibility(View.VISIBLE);
                updateBikerPresenter.resetPassword(biker.getEmail());
            }
        });

        View updateStockButton = rootView.findViewById(R.id.updateStockButton);
        updateStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isBlank(userStockEditText.getText().toString())) {
                    userStockEditText.setError(getString(R.string.stock_required));
                    userStockEditText.requestFocus();
                    return;
                }

                executingTaskProgressBar.setVisibility(View.VISIBLE);
                int stock = Integer.parseInt(userStockEditText.getText().toString());
                biker.setLastStock(stock);
                updateBikerPresenter.updateStock(biker);
            }
        });
    }
}
