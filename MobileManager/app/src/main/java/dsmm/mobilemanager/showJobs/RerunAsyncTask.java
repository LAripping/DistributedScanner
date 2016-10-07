package dsmm.mobilemanager.showJobs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.showSAs.MainActivity;

/**
 * Created by apostolis on 4/2/2016.
 */
public class RerunAsyncTask extends AsyncTask<Void,Void,Boolean>  {
    private Context context;
    private String job;

    public RerunAsyncTask(String job, Context context){
        this.job=job;
        this.context=context;
    }

    @Override
    public Boolean doInBackground(Void... params) {

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());
        if(!isOnline){
            cancel(true);
            return false;
        }


        try {
            final String url = MainActivity.am_url+ "/nmapjobs/rerun";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.postForObject(url,job,String.class);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success){
            Toast toast = Toast.makeText(context, "Nmap Job :"+job+" is running again", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(context, "An error occured", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}
