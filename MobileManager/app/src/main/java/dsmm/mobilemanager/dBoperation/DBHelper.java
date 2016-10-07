package dsmm.mobilemanager.dBoperation;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Vault.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DBContract.Contract.TABLE_NMAPJOBS + " (" +
                    DBContract.Contract.COLUMN_NAME_ID + " INTEGER," +
                    DBContract.Contract.COLUMN_NAME_PARAMS + TEXT_TYPE + COMMA_SEP +
                    DBContract.Contract.COLUMN_NAME_PERIOD + " INTEGER" + COMMA_SEP +
                    DBContract.Contract.COLUMN_NAME_PERIODIC + " INTEGER" + COMMA_SEP +
                    DBContract.Contract.COLUMN_NAME_SAHASH + TEXT_TYPE +
            " )";


    //Constructor
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // Log.e("SQL Mes:", " DONE DB HELPER CONSTRUCTOR .1 ");

    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v("SQL Mes:", " in method onCreate in class DBHelper .5" );
        db.execSQL(SQL_CREATE_TABLE);
        Log.v("SQL MES2:", "INSIDE AND DONE  .6");
        /*try
        {
            db.execSQL(SQL_CREATE_TABLE);
            Log.v("SQL MES2:", "INSIDE AND DONE");
        }
        catch (SQLException e)
        {
            Log.v("SQL problem:", " in method onCreate in class DBHelper " + e);
        }*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}


