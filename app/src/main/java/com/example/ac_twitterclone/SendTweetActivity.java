package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class SendTweetActivity extends AppCompatActivity {

    private EditText edtMyTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtMyTweet = findViewById(R.id.xedtTweet);


    }

    public void sendTweet  (View view){

        ParseObject parseObject = new ParseObject("MyTweets");
        parseObject.put("tweet",edtMyTweet.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    FancyToast.makeText(SendTweetActivity.this,"Your tweet has been sent",
                            FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

                } else {
                    FancyToast.makeText(SendTweetActivity.this,"Error: Tweet not sent"+ "\n"+ e.getMessage(),
                            FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                }
                progressDialog.dismiss();
            }   // end of done
        });     // end of saveInBackground
    }
}
