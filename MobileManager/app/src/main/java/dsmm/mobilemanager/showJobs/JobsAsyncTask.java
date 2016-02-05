package dsmm.mobilemanager.showJobs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import dsmm.mobilemanager.R;

/**
 * Created by apostolis on 24/1/2016.
 */
public class JobsAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private ArrayList<String> jobsArray;
    private String jobs;
    private String hash;
    private JobsActivity activity;


    public JobsAsyncTask(String hash, JobsActivity activity){
        this.hash=hash;
        this.activity = activity;
    }

    @Override
    public Boolean doInBackground(Void... params) {

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
            final String url = activity.getResources()
                    .getString(R.string.am_url) + "/nmapjobs";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            jobs = restTemplate.getForObject(url+"/"+hash, String.class);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success){
            jobs = jobs.replace("[","");
            jobs = jobs.replace("]","");

            if(jobs.equals("")){
                Toast toast = Toast.makeText(activity, "There are no active jobs", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                jobsArray = new ArrayList<String>();
                if (!jobsArray.isEmpty())
                {
                    jobsArray.clear();
                }
            }
            else{
                jobsArray = new ArrayList<String>( Arrays.asList(jobs.split(", ")) );
                int size = jobsArray.size();
                int j=0;
                for( int i=0 ; i < size;i++){
                    String job=jobsArray.get(i-j);
                    String[] param= job.split(",");

                    if(param[1].equals("Stop")) {
                        jobsArray.remove(i-j);
                        j++;
                    }
                }
            }
        }
        else
        {
            Toast toast = Toast.makeText(activity, "Disconnected, task cancelled", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        activity.JobsListUpdate(jobsArray);

    }
}
