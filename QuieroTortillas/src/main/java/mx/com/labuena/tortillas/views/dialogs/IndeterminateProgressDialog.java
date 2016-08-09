package mx.com.labuena.tortillas.views.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import mx.com.labuena.tortillas.models.DialogData;


/**
 * Created by clerks on 7/9/16.
 */

public class IndeterminateProgressDialog extends DialogFragment {
    public static final String DATA_DIALOG_ARGUMENT_ID =
            DialogData.class.getName();


    public static IndeterminateProgressDialog newInstance(DialogData datos) {
        IndeterminateProgressDialog dialog = new IndeterminateProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA_DIALOG_ARGUMENT_ID, datos);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final DialogData dialogData = this.getArguments()
                .getParcelable(DATA_DIALOG_ARGUMENT_ID);

        ProgressBar progressBar = new ProgressBar(this.getActivity());
        progressBar.setIndeterminate(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(
                getResources().getText(
                        dialogData.getResourceTitleId()))
                .setMessage(
                        getResources().getText(
                                dialogData.getResourceMessageId()))
                .setCancelable(dialogData.isCancelable())
                .setIcon(dialogData.getResourceIconId())
                .setView(progressBar);
        this.setCancelable(dialogData.isCancelable());
        return builder.create();
    }

}
