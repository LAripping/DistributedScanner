package dsmm.mobilemanager.users;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import dsmm.mobilemanager.dBoperation.ConnectivityReceiver;
import dsmm.mobilemanager.insertJob.AssignJobDialogFragment;
import dsmm.mobilemanager.showSAs.MainActivity;
import dsmm.mobilemanager.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    private EditText username;
    private EditText password;
    private Button login_button;
    private ConnectivityReceiver conRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager( this );
        if( session.isLoggedIn() ){
            String data = session.getSessionDetails();
            startActivity(new Intent(this, MainActivity.class).putExtra("data",data));
            finish();
        }

        setContentView(R.layout.activity_login);

        setTitle(getResources().getString(R.string.title_activity_login));

        //Create a DialogFragment to get AM's url
        DialogFragment newFragment = new AmUrlDialogFragment();

        //Show the DialogFragment to the user
        newFragment.show(getFragmentManager(), "amurl");

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login_button = (Button) findViewById(R.id.sign_in_button);

        final LoginAsyncTask lg = new LoginAsyncTask(this);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(username.getText().length()== 0 || password.getText().length()==0) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Invalid login please try again!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else{
                   lg.execute(new String[]{username.getText().toString(), password.getText().toString()}) ;
                }

            }
        });
    }


    public void registerb(View view) {
        Intent i=new Intent(getBaseContext(),RegisterActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(conRes==null){
            conRes = new ConnectivityReceiver();

            final IntentFilter infilt = new IntentFilter();
            infilt.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            registerReceiver(conRes,infilt );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(conRes!=null){
            unregisterReceiver(conRes);
            conRes = null;
        }
    }



}

