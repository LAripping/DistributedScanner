package dsmm.mobilemanager.users;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.insertJob.InsertJobAsyncTask;
import dsmm.mobilemanager.showSAs.MainActivity;
import dsmm.mobilemanager.showSAs.SAListFragment;

/**
 * Created by apostolis on 6/2/2016.
 */
public class AmUrlDialogFragment extends DialogFragment {

    private EditText editPort;
    private EditText editIP;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Initialize the fragment and retrieve the SA's hash
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View infLayout = inflater.inflate(R.layout.ip_dialog, null);
        builder.setView(infLayout);

        // Keep a reference for all views (used later)
        editIP = (EditText) infLayout.findViewById(R.id.ip);
        editPort = (EditText) infLayout.findViewById(R.id.port);

        // Add title and action buttons
        // Button listeners are NOT set in this stage (just stubs)
        builder.setTitle(R.string.ip_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {}
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
                    String ip = editIP.getText().toString();
                    String port = editPort.getText().toString();

                    MainActivity.am_url = "http://" + ip + ":" + port;

                    // Then close the Dialog
                    d.dismiss();
                }
            });
        }
    }


}
