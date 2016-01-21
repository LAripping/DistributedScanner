package dsMM.NetworkNotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkInternet(View view) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfoWiFi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(netInfoMobile.isConnected()){
            Toast.makeText(this, getResources().getString(R.string.conn_mobile), Toast.LENGTH_SHORT).show();
        } else if(netInfoWiFi.isConnected()){
            Toast.makeText(this, getResources().getString(R.string.conn_wifi), Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, getResources().getString(R.string.conn_no), Toast.LENGTH_SHORT).show();
        }
    }

}
