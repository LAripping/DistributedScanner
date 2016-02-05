package dsmm.mobilemanager.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dsmm.mobilemanager.MainActivity;
import dsmm.mobilemanager.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    private EditText username;
    private EditText password;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager( this );
        if( session.isLoggedIn() ){
            String data = session.getSessionDetails();
            startActivity(new Intent(this, MainActivity.class).putExtra("data",data));
            finish();
        }

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

