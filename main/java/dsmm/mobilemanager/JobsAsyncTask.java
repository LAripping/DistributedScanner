package dsmm.mobilemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apostolis on 24/1/2016.
 */
public class JobsAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private String[] nmap_jobs;
    private String jobs;
    private String hash;
    private Context context;


    public JobsAsyncTask(String hash, Context context){
        this.hash=hash;
        this.context = context;
    }

    @Override
    public Boolean doInBackground(Void... params) {

        try {
            final String url = context.getResources()
                    .getString(R.string.am_url) + "/nmapjobs";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            jobs = restTemplate.getForObject(url+"/"+hash, String.class);
            jobs.replace("[","");
            jobs.replace("]","");
            nmap_jobs = jobs.split(",");

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {

    }
}
