package dsMM.NetworkNotifier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tsaou on 23/1/2016.
 */
public class InsertJobAsyncTask extends AsyncTask<String,Void,Integer> {

    private Context context;

    public InsertJobAsyncTask(Context context){
        this.context = context;
    }

    @Override
    public void onPreExecute(){

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        if(!isOnline){
            // TODO add to SQLite database if no internet
            cancel(true);
        } else{
            Toast.makeText(context, "entered onPreExecute",Toast.LENGTH_SHORT);
            // TODO send SQLite contents to AM if any
            // 1)update AM with SQLite remnants
            // 2)delete them from DB
            // 3)proceed to InsertJobAsyncTask execution
        }
    }



// The Parameters expected are in a String[] where:
// [0]. contains the Job's info e.g. "-Ox -A -v,true,10"
// [1]. contains the hash of the SA the job will be assigned to
    @Override
    public Integer doInBackground(String... taskParams) {

        // Prepare the connection objects
        final URL AMurl;
        HttpURLConnection conn = null;
        OutputStreamWriter out = null;
        Uri.Builder b = new Uri.Builder();

        // Build the connection URL
        b.scheme("http").authority("192.168.1.3")     //TODO url manager dialog
                .appendPath("nmapjobs")
                .appendQueryParameter("hash", taskParams[0]);

        try {
            // Open a connection to send an HTTP-PUT request
            AMurl = new URL(b.build().toString());
            conn = (HttpURLConnection) AMurl.openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(15000 /* milliseconds */);

            // Fill the request body
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(taskParams[0].length());
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(taskParams[0], 0, taskParams[0].length());
            out.close();

            // Connect and get the response
            conn.connect();
            int response = conn.getResponseCode();
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        // A result Response of -1 indicates an error
        return -1;
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
            default:
                status = "Unexpected response (code "+response+") from AM!";
        }
        // Notify the user of the result
        Toast.makeText(context, status, Toast.LENGTH_LONG);
    }
}