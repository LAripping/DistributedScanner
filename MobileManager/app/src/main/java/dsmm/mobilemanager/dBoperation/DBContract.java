package dsmm.mobilemanager.dBoperation;


import android.provider.BaseColumns;

public class DBContract
{

//    Note: Because they can be long-running, be sure that you call getWritableDatabase() or getReadableDatabase() in a background thread, such as with AsyncTask or IntentService.

    public DBContract(){}

    /* Inner class that defines the table contents */
    public static abstract class Contract implements BaseColumns
    {
        public static final String TABLE_NMAPJOBS = "nmapjobs";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PARAMS = "params";
        public static final String COLUMN_NAME_PERIODIC = "periodic";
        public static final String COLUMN_NAME_PERIOD = "period";
        public static final String COLUMN_NAME_SAHASH = "hash";

    }



}
