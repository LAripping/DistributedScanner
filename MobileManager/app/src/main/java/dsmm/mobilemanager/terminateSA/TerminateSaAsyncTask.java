package dsmm.mobilemanager.terminateSA;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.dBoperation.DBoperations;

/**
 * Created by apostolis on 23/1/2016.
 */
public class TerminateSaAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    private String hash;
    private DBoperations dBoperations = null;

    public TerminateSaAsyncTask(Context context,String hash) {
        this.context=context;
        this.hash=hash;
        this.dBoperations = new DBoperations(context);
    }



    @Override
    public Boolean doInBackground(Void... params) {

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        if(!isOnline){
            String deleteJob = "-1,exit(0),1,1,"+hash;
            dBoperations.storeNmap(deleteJob);
            cancel(true);
            return false;
        }

        try {
            final String url = context.getResources()
                    .getString(R.string.am_url) + "/softwareagent";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.delete(url+"/"+hash);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(!aBoolean){
            Toast toast = Toast.makeText(context, "Disconnected, task cancelled", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
}
