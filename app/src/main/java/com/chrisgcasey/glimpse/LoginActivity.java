package com.chrisgcasey.glimpse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends Activity {

    EditText mUserName;
    EditText mPassword;
    Button mLoginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);

        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mUserName = (EditText) findViewById(R.id.userNameField);

        setUpSignUpTextListener();
        setUpButtonListener();
    }

    private void setUpButtonListener() {

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginUserName  = mUserName.getText().toString();
                String loginPassword = mPassword.getText().toString();

                loginUserName = loginUserName.trim();
                loginPassword = loginPassword.trim();






                if (loginUserName.isEmpty() | loginPassword.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(R.string.error_title);
                    builder.setMessage(getString(R.string.error_message));
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    setProgressBarIndeterminate(true);
                    ParseUser.logInInBackground(loginUserName, loginPassword, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            setProgressBarIndeterminate(false);
                            if(e == null){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                // build an alert dialog to handle a login error
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(R.string.error_title);
                                builder.setMessage(getString(R.string.login_error));
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        }
                    });
                    }

            }
        });
    }

    private void setUpSignUpTextListener(){
        TextView signUpText = (TextView) findViewById(R.id.signUpText);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
