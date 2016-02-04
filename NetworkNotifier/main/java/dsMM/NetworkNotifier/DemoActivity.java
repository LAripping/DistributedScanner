package dsMM.NetworkNotifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        String data = getIntent().getExtras().getString(MainActivity.KEY_DATA);
        String[] credentials = data.split(",");

        EditText mUsernameText = (EditText) findViewById(R.id.username_given);
        mUsernameText.setText(credentials[0]);

        EditText mPasswordText = (EditText) findViewById(R.id.password_given);
        mPasswordText.setText(credentials[1]);
    }

    public void logout(View view){
        SessionManager session = new SessionManager(this);
        session.logoutUser();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
