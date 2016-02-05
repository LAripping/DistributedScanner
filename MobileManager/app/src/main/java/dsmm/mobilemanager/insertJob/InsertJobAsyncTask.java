package dsmm.mobilemanager.insertJob;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.dBoperation.DBoperations;

/**
 * Created by tsaou on 23/1/2016.
 */
public class InsertJobAsyncTask extends AsyncTask<String,Void,Integer> {

    private Context context;
    private DBoperations dBoperations = null;


    public InsertJobAsyncTask(Context context)
    {
        this.context = context;
        this.dBoperations = new DBoperations(context);
    }



// The Parameters expected are in a String[] where:
// [0]. contains the Job's info e.g. "-Ox -A -v,true,10"
// [1]. contains the hash of the SA the job will be assigned to
    @Override
    public Integer doInBackground(String... taskParams) {

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        if(!isOnline){
            String job = "-1,"+taskParams[0]+","+taskParams[1];
            dBoperations.storeNmap(job);
            cancel(true);
            return -2;
        }


        try {
            final String url = context.getResources()
                    .getString(R.string.am_url) + "/nmapjobs"; //TODO url manager dialog
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> entity = new HttpEntity<String>(taskParams[0],headers);

            ResponseEntity<String> response = restTemplate.exchange(url + "?hash=" + taskParams[1], HttpMethod.PUT, entity,String.class);
            HttpStatus statuscode = response.getStatusCode();
            int status = statuscode.value();

            return status;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return -1;
        }


    }


// The Results expected is an Integer Response Code indicating
// whether the job was succesfully inserted in the AM or not.
    @Override
    protected void onPostExecute(final Integer response) {
        String status = null;

        switch(response){
            case HttpURLConnection.HTTP_OK:
                status = "Job assigned successfully!";
                break;
            case -1:
                status = "An error occured while trying to send the job";
                break;
            case -2:
                status = "Disconnected, task cancelled";
                break;
            default:
                status = "Unexpected response (code "+response+") from AM!";
                break;
        }
        // Notify the user of the result
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}