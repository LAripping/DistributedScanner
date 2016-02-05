package dsmm.mobilemanager.dBoperation;

import android.os.AsyncTask;

import dsmm.mobilemanager.MainActivity;

/**
 * Created by apostolis on 5/2/2016.
 */
public class DBAsyncTaskOpen extends AsyncTask<Void,Void,Void> {
    private MainActivity activity;

    public DBAsyncTaskOpen(MainActivity activity){
        this.activity=activity;
    }

     @Override
     public Void doInBackground(Void... params){
         DBoperations dBoperations = new DBoperations(activity);
         dBoperations.open();

         return null;
     }
}
