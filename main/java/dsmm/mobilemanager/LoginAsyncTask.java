package dsmm.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apostolis on 19/1/2016.
 */
public class LoginAsyncTask extends AsyncTask<String,Void,Boolean> {

    private Activity activity;
    private String data;

    public LoginAsyncTask(AppCompatActivity activity){
        this.activity=activity;
    }

    @Override
    public Boolean doInBackground(String... params){
        data=params[0]+","+params[1];

        try {

            final String url = activity.getResources().getString(R.string.am_url)
                    + "/users/login"; //TODO url manager dialog
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
        if(!success) {
            Toast toast = Toast.makeText(activity, "Invalid login please try again!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

            activity.startActivity(new Intent(activity,LoginActivity.class));
            activity.finish();

        }
        else{
            activity.startActivity(new Intent(activity,MainActivity.class).putExtra("data",data));
            activity.finish();
        }
    }
}
