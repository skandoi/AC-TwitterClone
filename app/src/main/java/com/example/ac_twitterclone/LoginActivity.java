package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLogin, btnSignUpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        edtLoginEmail = findViewById(R.id.xedtLoginEmail);
        edtLoginPassword = findViewById(R.id.xedtLoginPassword);

        // for execution of signup when the enter key is pressed on the keyboard
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });

        btnLogin = findViewById(R.id.xbtnLogin);
        btnSignUpActivity = findViewById(R.id.xbtnSignUpActivity);

        btnSignUpActivity.setOnClickListener(LoginActivity.this);
        btnLogin.setOnClickListener(LoginActivity.this);

        if (ParseUser.getCurrentUser() != null){
            transitionSocialMediaActivity(); // if already logged in transition and do not ParseUser.logOut();
        }



    } // end of onCreate

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.xbtnLogin:

                if (edtLoginEmail.getText().toString().equals("") ||
                        edtLoginPassword.getText().toString().equals("") ) {
                    FancyToast.makeText(LoginActivity.this, "Username or Password cannot be empty",
                            Toast.LENGTH_LONG, FancyToast.INFO, false).show();
                } else {

                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(),
                            edtLoginPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                    if (user != null && e == null) {
                                        FancyToast.makeText(LoginActivity.this, user.getUsername() + " is Logged In",
                                                Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                        transitionSocialMediaActivity();
                                    } else {
                                        FancyToast.makeText(LoginActivity.this, "There was an error: " + e.getMessage(),
                                                Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                    }

                                }
                            }); // end of LogInInBackground
                }

                break; // end of case xbtnLogin

            case R.id.xbtnSignUpActivity:

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break; // end of case xbtnSignUpActivity

        } //end of switch

    } //end of onClick

    public void loginActivityLayoutTapped(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e){
            e.printStackTrace();

        }

    } // end of rooLayoutTapped

    private void transitionSocialMediaActivity()  {

        Intent intent   = new Intent(LoginActivity.this,TwitterUsers.class);
        startActivity(intent);
        finish();

    }
}
