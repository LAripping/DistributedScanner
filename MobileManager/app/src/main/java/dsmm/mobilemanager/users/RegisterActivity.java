package dsmm.mobilemanager.users;

/**
 * Created by apostolis on 16/1/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dsmm.mobilemanager.R;

public class RegisterActivity extends ActionBarActivity {
    private EditText username;
    private EditText password;
    private EditText repassword;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle(getResources().getString(R.string.title_activity_register));

        username = (EditText) findViewById(R.id.reg_username);
        password = (EditText) findViewById(R.id.reg_password);
        repassword = (EditText) findViewById(R.id.reg_repassword);

        button = (Button) findViewById(R.id.btnRegister);

        final RegisterAsyncTask rq = new RegisterAsyncTask(this);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(username.getText().length()!= 0 && password.getText().length()!=0) {
                    if (password.getText().toString().equals(repassword.getText().toString())) {
                        rq.execute(new String[]{username.getText().toString(), password.getText().toString()});
                    } else {
                        Toast toast = Toast.makeText(RegisterActivity.this, "Please check again your Password and Retype Password", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();;
                    }
                }
                else {
                    Toast toast = Toast.makeText(RegisterActivity.this, "Invalid register please try again", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }

        });


    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getBaseContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }


}