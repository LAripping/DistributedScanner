package dsmm.mobilemanager.showJobs;

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

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.dBoperation.DBoperations;
import dsmm.mobilemanager.showSAs.MainActivity;

/**
 * Created by apostolis on 2/2/2016.
 */
public class DeleteJobAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private int id;
    private Context context;
    private DBoperations dBoperations = null;

    public DeleteJobAsyncTask(int id, Context context){
        this.id=id;
        this.context = context;
        this.dBoperations = new DBoperations(context);
    }

    @Override
    public Boolean doInBackground(Void... params) {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        if(!isOnline){
            String deleteJob = id+",Stop,1,1,dummyHash";
            dBoperations.storeNmap(deleteJob);
            cancel(true);
            return false;
        }


        try {
            final String url = MainActivity.am_url+ "/nmapjobs";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.delete(url + "/" + id);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success){
            Toast toast = Toast.makeText(context, "The job has been deleted", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(context, "An error occured", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}

