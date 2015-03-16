package com.example.conor.senan.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.conor.senan.R;

/**
 * Created by Conor on 30/01/2015.
 */
public class ConfirmDeleteAdDialog extends DialogFragment {

    public interface DeleteAddListener {
        void onDeleteAdd();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof DeleteAddListener)) {
            throw new ClassCastException(activity.toString() + " must implement DeleteAddListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_ad_message)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((DeleteAddListener)(getActivity())).onDeleteAdd();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                    }
                });
        return builder.create();
    }



}
