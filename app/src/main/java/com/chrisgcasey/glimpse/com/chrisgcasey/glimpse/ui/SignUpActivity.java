package com.chrisgcasey.glimpse.com.chrisgcasey.glimpse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.chrisgcasey.glimpse.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends Activity {

    EditText mNewUserName;
    EditText mNewPassword;
    EditText mNewEmail;
    Button mSaveButton;
    Button mCancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);

        getActionBar().hide();

        mNewUserName = (EditText) findViewById(R.id.newUserName);
        mNewPassword = (EditText) findViewById(R.id.newPassword);
        mNewEmail = (EditText) findViewById(R.id.newEmail);
        mSaveButton = (Button) findViewById(R.id.button_save);
        mCancelButton = (Button) findViewById(R.id.button_cancel);


        setUpButtonListener();
        //setUpFocusListener();

    }
    /**
    private void setUpFocusListener() {
        mNewUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });
        mNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                }

            }
        });
    }
     **/

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

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }



}
