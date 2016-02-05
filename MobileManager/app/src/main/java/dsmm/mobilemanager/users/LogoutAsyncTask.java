package dsmm.mobilemanager.users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 20/1/2016.
 */
public class LogoutAsyncTask extends AsyncTask<String,Void,Boolean> {

    private Context context;
    private String data;
    private Activity activity;

    public LogoutAsyncTask(Context context,Activity activity){
        this.context=context;
        this.activity=activity;
    }

    @Override
    public Boolean doInBackground(String... params){
        data=params[0]+","+params[1];

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        if(!isOnline){
            cancel(true);
            return false;
        }


        try {

            final String url = context.getResources()
                    .getString(R.string.am_url) + "/users/logout"; //TODO url manager dialog
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.postForObject(url, data, String.class);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            SessionManager session = new SessionManager(context);
            session.logoutUser();

            context.startActivity(new Intent(context, LoginActivity.class));
            activity.finish();
        }
        else{
            Toast toast = Toast.makeText(context, "Logout failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
}
