package dsmm.mobilemanager.showResults;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import dsmm.mobilemanager.R;
import dsmm.mobilemanager.showSAs.MainActivity;

/**
 * Created by apostolis on 20/1/2016.
 */
public class ResultsAsyncTask extends AsyncTask<Void,Void,Boolean> {

    private AlertDialog.Builder dialog;
    private final EditText input;
    private String results ;
    private TextView textView;
    private Context context;

    public ResultsAsyncTask(AlertDialog.Builder dialog,Context context,final EditText input) {
        this.dialog=dialog;
        this.context=context;
        this.input=input;
    }

    @Override
    public Boolean doInBackground(Void... params){

        //Check if there's internet connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());
        if(!isOnline){
            cancel(true);
            return false;
        }


        try {
            final String url = MainActivity.am_url+ "/results";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            results = restTemplate.getForObject(url, String.class);

            return true;
        } catch (Exception e) {
            Log.e("HttpRequestTask", e.getMessage(), e);
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            if(results.equals("[]")){
                Toast.makeText(context, "There are no results available", Toast.LENGTH_LONG).show();
            }
            else {

                dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String no = input.getEditableText().toString();
                        if(no.length()!=0){

                            int no1 = Integer.parseInt(no);
                            final Dialog res_dialog = new Dialog(context);

                            res_dialog.setContentView(R.layout.all_results);
                            textView = (TextView) res_dialog.findViewById(R.id.textView);

                            String[] all_res = results.split("</ResultEndsHere>");
                            if(no1>all_res.length) {
                                no1=all_res.length;
                            }
                            String[] show_res = new String[no1];
                            int j = 0;
                            for (int i = (all_res.length - no1); i < all_res.length; i++) {
                                show_res[j] = all_res[i];
                                j++;
                            }
                            textView.setText(Arrays.toString(show_res).replaceAll(", ", "\n"+"\n").replace("[", "").replace("]", ""));

                            Button dialogbtn = (Button) res_dialog.findViewById(R.id.ok_button);

                            dialogbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    res_dialog.dismiss();
                                }

                            });
                            res_dialog.setTitle("All Results");
                            res_dialog.show();
                        }
                        else{
                            Toast toast = Toast.makeText(context, "Please type a number", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }

        }
        else{
            Toast toast = Toast.makeText(context, "An error occured while trying to get results", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }

}
