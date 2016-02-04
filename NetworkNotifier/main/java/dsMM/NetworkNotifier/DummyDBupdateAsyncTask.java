package dsMM.NetworkNotifier;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

/**
 * Created by tsaou on 27/1/2016.
 */
public class DummyDBupdateAsyncTask extends AsyncTask<Void,Void,Void> {

    private int id = 1;
    private Context context;
    private NotificationManager mNotifyManager = null;
    private NotificationCompat.Builder mBuilder = null;

    public DummyDBupdateAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        // Create a notification
        mNotifyManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder
                = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Long operation")
                .setContentText("Operation in progress")
                .setSmallIcon(R.drawable.ic_compare_arrows_black_24dp);

        // Start the progress bar (intermediate type)
        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.build());
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Sleep for 10 seconds to simulate a DB update
            Thread.currentThread().sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){

        // When the Task finishes it replaces the progress Bar
        // with a "Task Complete" one
        mBuilder.setContentText("Operation complete")
                .setSmallIcon(R.drawable.ic_done_black_24dp)
                .setProgress(0, 0, false);
        mNotifyManager.notify(id, mBuilder.build());
    }
}
