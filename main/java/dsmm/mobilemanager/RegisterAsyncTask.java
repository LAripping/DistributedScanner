package dsmm.mobilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apostolis on 16/1/2016.
 */
public class RegisterAsyncTask extends AsyncTask <String,Void,Boolean> {
    private ActionBarActivity activity;
    private String request;

    public RegisterAsyncTask(ActionBarActivity activity){
        this.activity=activity;
    }

    @Override
    public Boolean doInBackground(String... params) {
        request=params[0]+","+params[1];

            try {

                final String url = "http://192.168.1.69:9998/users"; //TODO url manager dialog
                RestTemplate restTemplate = new RestTemplate();
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
