package dsmm.mobilemanager.dBoperation;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.showSAs.MainActivity;

/**
 * Created by apostolis on 5/2/2016.
 */
public class SendDbAsyncTask extends AsyncTask<Void,Void,Integer> {

    private int id = 1;
    private int lastStatus = 0;
    private Context context;
    private NotificationManager mNotifyManager = null;
    private NotificationCompat.Builder mBuilder = null;
    private DBoperations dBoperations = null;


    public SendDbAsyncTask(Context context){
        this.context = context;
        this.dBoperations = new DBoperations(context);
    }


    @Override
    protected void onPreExecute(){
        // Create a notification
        mNotifyManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder
                = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_compare_arrows);

        // Start the progress bar (intermediate type)
        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.build());
    }

    @Override
    public Integer doInBackground(Void... params){
        ArrayList<String> dbJobs = new ArrayList<String>();
        dbJobs = dBoperations.getAllNmaps();

        // Loop for every nmap returned from DB
        for(String s : dbJobs) {
            String[] remains = s.split(",");

            // If it was a "Terminate SA" command
            if(remains[1].equals("exit(0)"))
            {
                try {
                    final String url = MainActivity.am_url + "/softwareagent";
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                    restTemplate.delete(url+"/"+remains[4]);
                } catch (Exception e) {
                    Log.e("HttpRequestTask", e.getMessage(), e);
                    return -1;
                }
            }

            // If it was a "Delete Periodic Job" command
            else if(remains[1].equals("Stop"))
            {
                try {
                    final String url = MainActivity.am_url+  "/nmapjobs";
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    restTemplate.delete(url + "/" + id);
                } catch (Exception e) {
                    Log.e("HttpRequestTask", e.getMessage(), e);
                    return -1;
                }
            }

            // Otherwise, it's a plain Nmap Job
            else
            {
                String job = remains[1] + "," + remains[2] + "," + remains[3];
                String hash = remains[4];
                try {

                    final String url = MainActivity.am_url+  "/nmapjobs";
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    HttpEntity<String> entity = new HttpEntity<String>(job, headers);

                    ResponseEntity<String> response = restTemplate.exchange(url + "?hash=" + hash,
                            HttpMethod.PUT, entity, String.class);
                    HttpStatus statuscode = response.getStatusCode();
                    lastStatus = statuscode.value();

                } catch (Exception e) {
                    Log.e("HttpRequestTask", e.getMessage(), e);
                    return -1;
                }
            }


        }
        return  lastStatus;
    }


    @Override
    protected void onPostExecute(final Integer results){

        // When the Task finishes it replaces the progress Bar
        // with a "Task Complete" one
        if(results != -1) {
            mBuilder.setContentText(context.getString(R.string.operation_complete))
                    .setSmallIcon(R.drawable.ic_done)
                    .setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());

            dBoperations.deleteAllNmaps();
        }

        // OR a "Failed" one in case something went wrong
        else{
            mBuilder.setContentText(context.getString(R.string.operation_failed))
                    .setSmallIcon(R.drawable.ic_highlight_off)
                    .setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }
    }
}
