package dsmm.mobilemanager.dBoperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by petrakos on 25/1/2016.
 */
public class DBoperations
{

    private DBHelper helper;
    private SQLiteDatabase database;

    public DBoperations(Context context)
    {
        helper = new DBHelper(context);
        //Log.e("MESSAGE" , "DBOPS CONSTRUCTOR READY  .2");
    }


    public void open()
    {
       // Log.e("MESSAGE" , "INSIDE open .3");
        database = helper.getWritableDatabase();
        //Log.e("MESSAGE" , "INSIDE open AFTER GET WRITEABLE DATABASE  .7");
    }



    public boolean isEmpty()
    {
        database = helper.getWritableDatabase();
        //Log.e("MESSAGE" , "INSIDE IS EMPTY .8");

        //Log.e("MESSAGE" , "INSIDE IS EMPTY AFTER GET WRITEABLE DATABASE");
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " +DBContract.Contract.TABLE_NMAPJOBS, null);
        if(cursor != null)
        {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if(count > 0) {
                Log.v("MES" , "DATABASE IS NOT EMPTY .8");
                cursor.close();
                return false;
            }
        }
        cursor.close();
        Log.v("MES", "DATABASE IS EMPTY .8");
        return true;
    }


    //Store a nmap Job when needed
    public void storeNmap(String nmap) {
        database = helper.getWritableDatabase();
        String[] job = nmap.split(",");
        int id = Integer.parseInt(job[0]);
        String params = job[1];
        int periodic = job[2].equals("true") ? 1 : 0;
        int period;
        //(job[2] == null) ? period = 0 : period = Integer.parseInt(job[2]);

        if (periodic == 1) {
            Log.v("TIME", job[2]);
            period = Integer.parseInt(job[3]);
            //if (job[2] == null)     period = 0;
            //else                    period = Integer.parseInt(job[2]);
        } else period = 0;

        String sahash = job[4];

        //pass the values to be stored
        ContentValues values = new ContentValues();
        values.put(DBContract.Contract.COLUMN_NAME_ID, id);
        values.put(DBContract.Contract.COLUMN_NAME_PARAMS, params);
        values.put(DBContract.Contract.COLUMN_NAME_PERIOD, period);
        values.put(DBContract.Contract.COLUMN_NAME_PERIODIC, periodic);
        values.put(DBContract.Contract.COLUMN_NAME_SAHASH, sahash);
        long checkID = database.insert(DBContract.Contract.TABLE_NMAPJOBS, null, values);

        if (checkID == -1) {
            Log.v("SQLERROR", "Error inserting nmap in database");
        }
        else
        {
            Log.e("TRANSACTION", "Success in storing nmap jobs");
        }
    }


        // Returns all DB contents.
        // Each string is in form "id,params,periodic,period,hash"
        public ArrayList<String> getAllNmaps() {
            database = helper.getWritableDatabase();
            //SQLiteDatabase db = helper.getReadableDatabase();

            //the columns i am asking for
            String[] projection = {
                    DBContract.Contract.COLUMN_NAME_ID,
                    DBContract.Contract.COLUMN_NAME_PARAMS,
                    DBContract.Contract.COLUMN_NAME_PERIOD,
                    DBContract.Contract.COLUMN_NAME_PERIODIC,
                    DBContract.Contract.COLUMN_NAME_SAHASH
            };

            //execute query
            Cursor cursor = database.query(
                    DBContract.Contract.TABLE_NMAPJOBS,
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                     // The sort order
            );

            int params_index = cursor.getColumnIndex(DBContract.Contract.COLUMN_NAME_PARAMS);
            int period_index = cursor.getColumnIndex( DBContract.Contract.COLUMN_NAME_PERIOD);
            int periodic_index = cursor.getColumnIndex( DBContract.Contract.COLUMN_NAME_PERIODIC);
            int ID_index = cursor.getColumnIndex(DBContract.Contract.COLUMN_NAME_ID );
            int hash_index = cursor.getColumnIndex(DBContract.Contract.COLUMN_NAME_SAHASH);
            ArrayList<String> buffer = new ArrayList<>();

            if(cursor.getCount() == 0)
            {
                return null;
            }
            else {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(ID_index);
                    String params = cursor.getString(params_index);
                    int period = cursor.getInt(period_index);
                    int periodic = cursor.getInt(periodic_index);
                    String hash = cursor.getString(hash_index);
                    buffer.add(id + "," + params + "," + period + "," + periodic + "," + hash);
                }
            }
            Log.e("TRANSACTION", "Success in getting nmap jobs");
            return buffer;
        }

    public void deleteAllNmaps()
    {
        database = helper.getWritableDatabase();
        int rows = database.delete(DBContract.Contract.TABLE_NMAPJOBS ,null , null);
        Log.v("DELETE", "Number of rows affected is: " + rows);
    }

}
