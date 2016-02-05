package dsmm.mobilemanager.insertJob;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import dsmm.mobilemanager.MainActivity;
import dsmm.mobilemanager.R;
import dsmm.mobilemanager.SAListFragment;

/**
 * Created by tsaou on 22/1/2016.
 */
public class AssignJobDialogFragment extends DialogFragment {

    private String sa_hash;

    private EditText editParams;
    private Switch periodicSwitch;
    private EditText periodPicker;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initialize the fragment and retrieve the SA's hash
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        sa_hash = this.getArguments().getString(SAListFragment.KEY_SAHASH);

        // Inflate and set the layout for the dialog
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View infLayout = inflater.inflate(R.layout.dialog_assign, null);
        builder.setView(infLayout);

        // Keep a reference for all views (used later)
        editParams = (EditText) infLayout.findViewById(R.id.edit_params);
        periodicSwitch = (Switch) infLayout.findViewById(R.id.periodic_swich);
        periodPicker = (EditText) infLayout.findViewById(R.id.period_picker);

        // Add title and action buttons
        // Button listeners are NOT set in this stage (just stubs)
        builder.setTitle(R.string.specify_job);
        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.setNeutralButton(R.string.reset, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cancel dialog
                AssignJobDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Implementing onStart() as well to override the default cancelling
        // behaviour of all buttons inside a dialog!

        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            // The actual button listeners are set here

            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the values entered by the user
                    String params = editParams.getText().toString();
                    Boolean periodic = periodicSwitch.isChecked();
                    Integer period = null;

                    // Make sure they are valid
                    if ( !params.contains("-oX") ) {
                        editParams.setError("Parameters should contain \'-oX\' !");
                        return;
                    }
                    if (periodic == true ) {
                        if (periodPicker.getText().toString().equals("")) {
                            periodPicker.setError("Please specify a re-execution period!");
                            return;
                        } else {
                            period = Integer.parseInt(periodPicker.getText().toString());
                        }

                        if (period <= 0) {
                            periodPicker.setError("Re-execution period should be a positive value!");
                            return;
                        }
                    }

                    // Encode them to a "job-string" and pass them to the AM
                    // through an AsyncTask including the recipient SA's hash
                    InsertJobAsyncTask ij = new InsertJobAsyncTask(AssignJobDialogFragment.this.getActivity());
                    String job = params + ',' + periodic.toString() + ',' + period;
                    ij.execute(new String[]{job, sa_hash});

                    // Then close the Dialog
                    d.dismiss();
                }
            });

            Button neutralButton = (Button) d.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reset all values in the views
                    editParams.setText("");
                    periodicSwitch.setChecked(false);

                    // Here we DO NOT want to close the Dialog
                    //d.dismiss();
                }
            });
        }
    }


}