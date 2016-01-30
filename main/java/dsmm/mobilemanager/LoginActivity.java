package dsmm.mobilemanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    private EditText username;
    private EditText password;
    private Button login_button;

    //TODO:eite caps eite mikro to pairnei
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(getResources().getString(R.string.title_activity_login));

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login_button = (Button) findViewById(R.id.sign_in_button);

        final LoginAsyncTask lg = new LoginAsyncTask(this);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(username.getText().length()== 0 || password.getText().length()==0) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Invalid login please try again!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else{
                   lg.execute(new String[]{username.getText().toString(), password.getText().toString()}) ;
                }

            }
        });
    }
    public void registerb(View view) {
        Intent i=new Intent(getBaseContext(),RegisterActivity.class);
        startActivity(i);
        finish();
    }

}

