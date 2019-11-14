package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtSignUpEmail, edtSignUpUsername, edtSignUpPassword;
    private Button btnSignup, btnLoginActivity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SignUp");

        edtSignUpEmail = findViewById(R.id.xedtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.xedtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.xedtSignupPassword);

        // for execution of signup when the enter key is pressed on the keyboard
        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSignup);
                }
                return false;
            }
        });

        btnLoginActivity = findViewById(R.id.xbtnLoginActivity);
        btnSignup = findViewById(R.id.xbtnSignUp);

        btnSignup.setOnClickListener(this);
        btnLoginActivity.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
            transitionSocialMediaActivity(); // if already logged in transition and do not ParseUser.logOut();
        }


/*      // below code in basic parse server tutorial - (for twitter)
        ParseInstallation.getCurrentInstallation().saveInBackground();
*/
    }  // end of onCreate

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.xbtnSignUp:

                if (edtSignUpEmail.getText().toString().equals("") ||
                        edtSignUpUsername.getText().toString().equals("") ||
                        edtSignUpPassword.getText().toString().equals("")) {
                    FancyToast.makeText(MainActivity.this, "Email or Username or Password cannot be empty",
                            Toast.LENGTH_LONG, FancyToast.INFO, false).show();
                }  else    {
                    final ParseUser newUser = new ParseUser();
                    newUser.setEmail(edtSignUpEmail.getText().toString());
                    newUser.setPassword(edtSignUpPassword.getText().toString());
                    newUser.setUsername(edtSignUpUsername.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up " + newUser.getUsername());
                    progressDialog.show();

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                FancyToast.makeText(MainActivity.this, newUser.getUsername() + " is signed up",
                                        Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                transitionSocialMediaActivity();
                            } else {
                                FancyToast.makeText(MainActivity.this, "There was an error: " + e.getMessage(),
                                        Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                            progressDialog.dismiss();

                        }
                    }); // end of SignUpCallback
                } // end of if/else blank fields.


                break;  // end of case xbtnSignUp

            case R.id.xbtnLoginActivity:

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


                break; // end of case xbtnLoginActivity

        } //end of switch

    }   //end of onClick

    public void rootLayoutTapped(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e){
            e.printStackTrace();

        }

    } // end of rooLayoutTapped

    private void transitionSocialMediaActivity()  {

        Intent intent   = new Intent(MainActivity.this,TwitterUsers.class);
        startActivity(intent);
        finish();

    }

}
