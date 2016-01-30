package dsmm.mobilemanager;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by apostolis on 23/1/2016.
 */
public class SAsAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private String SAs;
    private MainActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> hasharray;


    public SAsAsyncTask(MainActivity activity,
                        SwipeRefreshLayout swipeRefreshLayout) {
        this.activity = activity;

        //TODO try this code please alongside the other todo in this file
        /*
            hasharray = new ArrayList<>();
        */
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public Boolean doInBackground(Void... params)
    {
        try {
            final String url = activity.getResources()
                    .getString(R.string.am_url) + "/softwareagent";
            RestTemplate restTemplate = new RestTemplate();
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

                //TODO instead of creating a new arraylist try the following
                if (!hasharray.isEmpty())
                {
                    hasharray.clear();
                }
            }
            else
            {
                //TODO finally replace the following with the code in /* .. */ so as not to say "new Arraylist" again
                hasharray = new ArrayList<String>( Arrays.asList( SAs.split(",") ) );
/*
                String [] temp = SAs.split(",");
                hasharray.clear();
                for(String s : temp)
                {
                    hasharray.add(s);
                }
*/
            }
        }

        //just pass the updated arraylist so as to update the listview
        activity.ListUpdate(hasharray);

        // If user requested a refresh, stop the animation
        //swipeRefreshLayout.setRefreshing(false);
    }

}
