package dsmm.mobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    final Context context=this;
    private String data;
    private String username;
    private String password;
    private Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        data = intent.getExtras().getString("data");
        String[] details = data.split(",");
        username = details[0];
        password=details[1];

        logoutbtn = (Button) findViewById(R.id.buttonLogout);

        final LogoutAsyncTask lg = new LogoutAsyncTask(this);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lg.execute(new String[]{username, password}) ;
            }
        });
    }

    public void pressD(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Terminate SA");
        builder.setMessage("Are you sure ?");

        final EditText input=new EditText(context);
        builder.setView(input);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
               String srt= input.getEditableText().toString();
                Toast.makeText(MainActivity.this, "You clicked yes button and you wrote "+srt, Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String srt = input.getEditableText().toString();
                Toast.makeText(MainActivity.this, "You clicked no button and you wrote " + srt, Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



}
