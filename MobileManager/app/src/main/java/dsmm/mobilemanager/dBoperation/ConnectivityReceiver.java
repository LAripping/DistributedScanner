package dsmm.mobilemanager.dBoperation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast indicating Connectivity change.

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = (wifi != null && wifi.isConnectedOrConnecting() )
                            || (mobile != null && mobile.isConnectedOrConnecting() );

        if(isConnected) {
            Log.e("Received an intent", "NOW CONNECTED");
            DBoperations dBoperations = new DBoperations(context);

            if(!dBoperations.isEmpty()){
                SendDbAsyncTask sendDbAsyncTask = new SendDbAsyncTask(context);
                sendDbAsyncTask.execute();
            }
        } else {
            Log.e("Received an intent","DISCONNECTED");
        }
    }
}
