package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtMyTweet;
    private Button btnViewTweets;

    private ListView listViewTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtMyTweet = findViewById(R.id.xedtTweet);
        btnViewTweets = findViewById(R.id.xbtnViewTweets);
        listViewTweets = findViewById(R.id.xlistViewTweets);

        btnViewTweets.setOnClickListener(this);

//        HashMap<String,Integer> hashMap = new HashMap<>();
//        hashMap.put("key1",1);
//        hashMap.put("key2",2);
//        FancyToast.makeText(this,hashMap.get("key2")+"", FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();


    }   // end of onCreate

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
    }   // end of sendTweet

    @Override
    public void onClick(View view) {

        final ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this,tweetList,
                android.R.layout.simple_list_item_2,new String[]{"tweetUserName","userTweet"},
                new  int[]{android.R.id.text1,android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweets");
            parseQuery.whereContainedIn("user",ParseUser.getCurrentUser().getList("followList"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() >0 && e== null ){
                        for (ParseObject tweetObject : objects){
                            HashMap<String,String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName",tweetObject.getString("user")+"\t"+ tweetObject.getCreatedAt());
                            userTweet.put("userTweet",tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }   // end of for
                        listViewTweets.setAdapter(adapter);
                    }   // end of if
                }   // end of done
            }); // end of findInBackground
        }catch (Exception e){
            e.printStackTrace();
        }   // end of try/catch



    }   // end of onClick
}
