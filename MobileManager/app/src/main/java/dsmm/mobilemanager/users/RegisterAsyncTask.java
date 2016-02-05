package dsmm.mobilemanager.users;

import android.content.Context;
import android.content.Intent;
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

import dsmm.mobilemanager.MainActivity;
import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 16/1/2016.
 */
public class RegisterAsyncTask extends AsyncTask <String,Void,Boolean> {
    private ActionBarActivity activity;
    private String request;
    private boolean discon;

    public RegisterAsyncTask(ActionBarActivity activity){
        this.activity=activity;
    }

    @Override
    public Boolean doInBackground(String... params) {
        request=params[0]+","+params[1];

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        if(!isOnline){
            cancel(true);
            discon = true;
            return false;
        }
            try {

                final String url = activity.getResources()
                        .getString(R.string.am_url) + "/users"; //TODO url manager dialog
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.put(url, request);

                return true;
            } catch (Exception e) {
                Log.e("HttpRequestTask", e.getMessage(), e);
                return false;
            }

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(!success) {
            Toast toast = Toast.makeText(activity, "Your Request has been rejected", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            activity.startActivity(new Intent(activity, RegisterActivity.class));
            activity.finish();
        }
        else{
            activity.startActivity(new Intent(activity,MainActivity.class).putExtra("data",request));
            activity.finish();
        }
    }





}
