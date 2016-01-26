package dsMM.NetworkNotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static final String KEY_SAHASH = "demo_hash";
        // TODO replace demo with the hash of the intended SA's



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Only used for this demo
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
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


/*  public void assignJob(String sa_hash) */
    public void assignJob(View view){
        //Create a DialogFragment to get new Job info
        DialogFragment newFragment = new AssignJobDialogFragment();

        //Pass the selected SA's hash to the DialogFragment
        Bundle fragArgs = new Bundle();
        fragArgs.putString(KEY_SAHASH, /* sa_hash */ "blashblash");
        newFragment.setArguments(fragArgs);

        //Show the DialogFragment to the user
        newFragment.show(getSupportFragmentManager(), "assignjob");
    }
}
