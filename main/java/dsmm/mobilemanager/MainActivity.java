package dsmm.mobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    final Context context=this;
    private String data;
    private String username;
    private String password;
    private Button logoutbtn;
    private ArrayList<String> hasharray;
    private SAListFragment fragment;
    private SAsAsyncTask mSAsAsyncTask;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private PopupAdapter myPopupAdapter;
    private ListView SaList;

    public PopupAdapter getMyPopupAdapter(){
        return myPopupAdapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the adapter with an empty arraylist
        fragment = (SAListFragment) getFragmentManager().findFragmentById(R.id.SaListFragment);
        hasharray = new ArrayList<>();
        myPopupAdapter = new PopupAdapter(hasharray,this,fragment);
        //SaList = fragment.getListView();
        fragment.setListAdapter(myPopupAdapter);

        mSAsAsyncTask = new SAsAsyncTask(MainActivity.this,mySwipeRefreshLayout /*, myPopupAdapter*/);
        mSAsAsyncTask.execute();

        //TODO keep this code if you want
       /* mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSAsAsyncTask = new SAsAsyncTask(MainActivity.this, mySwipeRefreshLayout, myPopupAdapter);
                        mSAsAsyncTask.execute();
                    }
                }
        );
*/


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


    //THIS METHOD IS NOW USEFUL
    public void ListUpdate(ArrayList<String> new_hasharray)
    {
        myPopupAdapter.clear();
        myPopupAdapter.addAll(new_hasharray);
        myPopupAdapter.notifyDataSetChanged();
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

        //TODO put this code in the refresh button

        mSAsAsyncTask = new SAsAsyncTask(MainActivity.this, mySwipeRefreshLayout/*, myPopupAdapter*/);
        mSAsAsyncTask.execute();


        /*
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Results");
        dialog.setMessage("How many last results do you want to see?");
        final EditText input=new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        dialog.setView(input);
        final ResultsAsyncTask rs = new ResultsAsyncTask(dialog,this,input);
        rs.execute();
        */
    }


}
