package dsMM.NetworkNotifier;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private View mProgressBar = null;
    public static final String KEY_SAHASH = "demo_hash";
    public static final String KEY_DATA = "demo_data";



    @Override
    protected void onCreate(Bundle State) {
        super.onCreate(State);

        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        SessionManager session = new SessionManager(this);
        if( session.isLoggedIn() ){
            String data = session.getSessionDetails();

            Intent demoIntent = new Intent(this, DemoActivity.class);
            demoIntent.putExtra(KEY_DATA, data);
            startActivity(demoIntent);
            finish();
        }
    }




    public void demo_checkInternet(View view) {
        ConnectivityManager connMgr
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMobile
                = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfoWiFi
                = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(netInfoMobile.isConnected()){
            Toast.makeText(this, getResources().getString(R.string.conn_mobile),
                    Toast.LENGTH_SHORT).show();
        } else if(netInfoWiFi.isConnected()){
            Toast.makeText(this, getResources().getString(R.string.conn_wifi),
                    Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, getResources().getString(R.string.conn_no),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // The actual method ALL AsyncTasks shoud call to check internet connectivity
    // while inside onPreExecute()
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


/*  public void assignJob(String sa_hash) */
    public void assignJob(View view){
        // Create a DialogFragment to get new Job info
        DialogFragment newFragment = new AssignJobDialogFragment();

        // Pass the selected SA's hash to the DialogFragment
        Bundle fragArgs = new Bundle();
        fragArgs.putString(KEY_SAHASH, /* sa_hash */ "blashblash");
        newFragment.setArguments(fragArgs);

        // Show the DialogFragment to the user
        newFragment.show(getSupportFragmentManager(), "assignjob");
    }



    // Long operation (update AM with SQLite contents) to be performed in
    // the background, offering a notification with an indirect progress-bar
    public void notificationOp(View view){
        // Start a dummy AsyncTask and execute it
        DummyDBupdateAsyncTask dbUpdateTask
                = new DummyDBupdateAsyncTask(getBaseContext());
        dbUpdateTask.execute();
    }


    // Long operation (populate view with AM contents) that might lag UI thread
    // instead a "loading" progress-animation is shown untill it's ready
    public void animationOp(View view){
        // Start a dummy AsyncTask and execute it


        DummyFetchFromAMAsyncTask fetchAMTask
                = new DummyFetchFromAMAsyncTask(mProgressBar);
        fetchAMTask.execute();
    }


    // Login user and start DemoActivity (Main)
    public void login(View view){
        EditText mUsername = (EditText) findViewById(R.id.give_username);
        String username = mUsername.getText().toString();

        EditText mPassword = (EditText) findViewById(R.id.give_password);
        String password = mPassword.getText().toString();


        SessionManager session = new SessionManager(this);
        session.loginUser(username, password);

        Intent demoIntent = new Intent(this, DemoActivity.class);
        demoIntent.putExtra(KEY_DATA, username + ',' + password);
        startActivity(demoIntent);
    }




}
