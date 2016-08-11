package mx.com.labuena.tortillas.views.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import mx.com.labuena.tortillas.events.ConfirmationAcceptedEvent;
import mx.com.labuena.tortillas.events.ConfirmationCanceledEvent;
import mx.com.labuena.tortillas.models.DialogData;
import mx.com.labuena.tortillas.setup.LaBuenaApplication;
import mx.com.labuena.tortillas.setup.LaBuenaModules;

/**
 * Created by moracl6 on 8/11/2016.
 */

public class ConfirmationDialogFragment extends android.support.v4.app.DialogFragment {
    public static final String DATA_DIALOG_ARGUMENT_ID =
            DialogData.class.getName();

    @Inject
    EventBus eventBus;


    public static ConfirmationDialogFragment newInstance(DialogData datos) {
        ConfirmationDialogFragment dialog = new ConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA_DIALOG_ARGUMENT_ID, datos);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final DialogData dialogData = this.getArguments()
                .getParcelable(DATA_DIALOG_ARGUMENT_ID);

        LaBuenaModules modules = LaBuenaApplication.getObjectGraph(getActivity()
                .getApplicationContext());
        modules.inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(
                getResources().getText(
                        dialogData.getResourceTitleId()));
        builder.setMessage(dialogData.getMessage());

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventBus.post(new ConfirmationAcceptedEvent());
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventBus.post(new ConfirmationCanceledEvent());
                    }
                });

        return builder.create();
    }

}
