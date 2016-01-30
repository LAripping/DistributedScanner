package dsmm.mobilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

        try {

           // HttpHeaders headers = new HttpHeaders();
            //headers.set("Connection","Close");

            final String url = context.getResources()
                    .getString(R.string.am_url) + "/users/logout"; //TODO url manager dialog
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            //HttpEntity<String> dataEntity = new HttpEntity<String>(data,headers);
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            //restTemplate.exchange(url, HttpMethod.POST,dataEntity,String.class);
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
