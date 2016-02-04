package dsMM.NetworkNotifier;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Layout;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by tsaou on 27/1/2016.
 */
public class DummyFetchFromAMAsyncTask extends AsyncTask<Void, Void, Void> {

    private View progressBar;

    public DummyFetchFromAMAsyncTask(View mProgressBar){
        this.progressBar = mProgressBar;
    }

    @Override
    protected void onPreExecute() {

        // Show loading animation while content is loading
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Sleep for 5 seconds to simulate content fetching from AM
            Thread.currentThread().sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){

        // Dismiss the loading animation
        progressBar.setVisibility(View.GONE);
    }
}
