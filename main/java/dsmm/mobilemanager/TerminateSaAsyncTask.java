package dsmm.mobilemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apostolis on 23/1/2016.
 */
public class TerminateSaAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    private String hash;

    public TerminateSaAsyncTask(Context context,String hash) {
        this.context=context;
        this.hash=hash;
    }



    @Override
    public Boolean doInBackground(Void... params) {

        try {
            final String url = context.getResources()
                    .getString(R.string.am_url) + "/softwareagent";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(url+"/"+hash);


            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }
    }

}
