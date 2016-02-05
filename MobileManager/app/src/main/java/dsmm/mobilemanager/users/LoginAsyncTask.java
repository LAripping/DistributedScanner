package dsmm.mobilemanager.users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;

import dsmm.mobilemanager.MainActivity;
import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 19/1/2016.
 */
public class LoginAsyncTask extends AsyncTask<String,Void,Boolean> {

    private Activity activity;
    private String data;
    private String response;
    private boolean discon = false;

    public LoginAsyncTask(AppCompatActivity activity){
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
            discon = true;
            return false;
        }

        try {

            final String url = activity.getResources().getString(R.string.am_url)
                    + "/users/login"; //TODO url manager dialog
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            response = restTemplate.postForObject(url, data, String.class);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(!success) {
            if(discon){
                Toast toast = Toast.makeText(activity, "Disconected!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                return;
            }

            Toast toast = Toast.makeText(activity, "Invalid login please try again!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

            activity.startActivity(new Intent(activity,LoginActivity.class));
            activity.finish();

        }
        else{
            switch(response){
                case "ok":
                    SessionManager session = new SessionManager( activity );
                    String[] credentials = data.split(",");
                    session.loginUser( credentials[0],credentials[1] );

                    activity.startActivity(new Intent(activity,MainActivity.class).putExtra("data",data));
                    activity.finish();
                    break;
                case "second time":
                    Toast toast = Toast.makeText(activity, "This user has already singed in", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                    activity.startActivity(new Intent(activity,LoginActivity.class));
                    activity.finish();
                    break;
                case "error":
                    toast = Toast.makeText(activity, "Invalid login please try again!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();

                    activity.startActivity(new Intent(activity,LoginActivity.class));
                    activity.finish();
            }

        }
    }
}
