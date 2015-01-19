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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends Activity {

    EditText mNewUserName;
    EditText mNewPassword;
    EditText mNewEmail;
    Button mSaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);

        mNewUserName = (EditText) findViewById(R.id.newUserName);
        mNewPassword = (EditText) findViewById(R.id.newPassword);
        mNewEmail = (EditText) findViewById(R.id.newEmail);
        mSaveButton = (Button) findViewById(R.id.saveButton);


        setUpButtonListener();

    }

    private void setUpButtonListener() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mNewUserName.getText().toString().trim();
                String password = mNewPassword.getText().toString().trim();
                String email = mNewEmail.getText().toString().trim();

                if(userName.isEmpty() | password.isEmpty() | email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle(R.string.error_title);
                    builder.setMessage(getString(R.string.error_message));
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    setProgressBarIndeterminate(true);
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(userName);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setProgressBarIndeterminate(false);
                            if(e == null) {

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                AlertDialog.Builder errorBuilder = new AlertDialog.Builder(SignUpActivity.this);
                                errorBuilder.setTitle(getString(R.string.sign_up_error_title));
                                errorBuilder.setMessage(getString(R.string.sign_up_error_message));
                                AlertDialog parseAlert = errorBuilder.create();
                                parseAlert.show();

                            }

                        }
                    });

                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
