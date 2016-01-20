package dsmm.mobilemanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apostolis on 20/1/2016.
 */
public class LogoutAsyncTask extends AsyncTask<String,Void,Boolean> {

    private AppCompatActivity activity;
    private String data;

    public LogoutAsyncTask(AppCompatActivity activity){
        this.activity=activity;
    }

    @Override
    public Boolean doInBackground(String... params){
        data=params[0]+","+params[1];

        try {

            final String url = "http://192.168.1.69:9998/users/logout"; //TODO url manager dialog
            RestTemplate restTemplate = new RestTemplate();
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
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        }
        else{
            Toast toast = Toast.makeText(activity, "Logout failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
}
