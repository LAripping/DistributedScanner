package dsmm.mobilemanager.showSAs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.dBoperation.ConnectivityReceiver;
import dsmm.mobilemanager.showResults.ResultsAsyncTask;
import dsmm.mobilemanager.users.LogoutAsyncTask;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    final Context context=this;
    private String data;
    private String username;
    private String password;
    private Button logoutbtn;
    private ArrayList<String> hasharray;
    private SAListFragment fragment;
    private FragmentManager fragman;
    private SAsAsyncTask mSAsAsyncTask;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private PopupAdapter myPopupAdapter;
    private ConnectivityReceiver conRes = null;

    public static String am_url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Mobile Manager");

        // Instantiate the adapter with an empty arraylist
        fragman = this.getFragmentManager();
        fragment = (SAListFragment) fragman.findFragmentById(R.id.SaListFragment);
        hasharray = new ArrayList<>();
        myPopupAdapter = new PopupAdapter(hasharray,this,fragment);
        //SaList = fragment.getListView();
        fragment.setListAdapter(myPopupAdapter);

        mSAsAsyncTask = new SAsAsyncTask(MainActivity.this);
        mSAsAsyncTask.execute();

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mySwipeRefreshLayout.setOnRefreshListener(this);

        //Logout
        Intent intent = getIntent();
        data = intent.getExtras().getString("data");
        String[] details = data.split(",");
        username = details[0];
        password = details[1];
        logoutbtn = (Button) findViewById(R.id.buttonLogout);
        final LogoutAsyncTask lg = new LogoutAsyncTask(this, this);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lg.execute(new String[]{username, password});
            }
        });
    }

    @Override
    public void onRefresh() {
        mSAsAsyncTask = new SAsAsyncTask(MainActivity.this);
        mSAsAsyncTask.execute();
    }

   /* @Override
    public void onResume(){
        super.onResume();
        new DBAsyncTaskOpen(this).execute();
    }
*/

    //THIS METHOD IS NOW USEFUL
    public void ListUpdate(ArrayList<String> new_hasharray)
    {
        myPopupAdapter.clear();
        myPopupAdapter.addAll(new_hasharray);
        myPopupAdapter.notifyDataSetChanged();
        if (mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }


    public void checkInternet(View view) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfoWiFi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(netInfoMobile.isConnected()){
            Toast.makeText(this, getResources().getString(R.string.conn_mobile), Toast.LENGTH_SHORT).show();
        } else if(netInfoWiFi.isConnected()){
            Toast.makeText(this, getResources().getString(R.string.conn_wifi), Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, getResources().getString(R.string.conn_no), Toast.LENGTH_SHORT).show();
        }
    }


    public void showResults(View view){

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("All Results");
        dialog.setMessage("How many last results do you want to see?");
        final EditText input=new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        dialog.setView(input);
        final ResultsAsyncTask rs = new ResultsAsyncTask(dialog,this,input);
        rs.execute();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(conRes==null){
            conRes = new ConnectivityReceiver();

            final IntentFilter infilt = new IntentFilter();
            infilt.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            registerReceiver(conRes,infilt );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(conRes!=null){
            unregisterReceiver(conRes);
            conRes = null;
        }
    }



}
