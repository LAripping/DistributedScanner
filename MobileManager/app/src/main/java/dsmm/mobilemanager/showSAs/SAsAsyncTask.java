package dsmm.mobilemanager.showSAs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 23/1/2016.
 */
public class SAsAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private String SAs;
    private MainActivity activity;
    private ArrayList<String> hasharray;


    public SAsAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public Boolean doInBackground(Void... params)
    {
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
            final String url = MainActivity.am_url+ "/softwareagent";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            SAs = restTemplate.getForObject(url, String.class);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            SAs = SAs.replace("[", "");
            SAs = SAs.replace("]", "");

            if(SAs.equals("There are no SAs registered")){
                Toast toast = Toast.makeText(activity, "There are no SAs registered", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                hasharray = new ArrayList<String>();
                if (!hasharray.isEmpty())
                {
                    hasharray.clear();
                }
            }
            else
            {
                hasharray = new ArrayList<String>( Arrays.asList( SAs.split(",") ) );

            }
            //just pass the updated arraylist so as to update the listview
            activity.ListUpdate(hasharray);

        }
        else {
            Toast toast = Toast.makeText(activity, "Disconect!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

}
