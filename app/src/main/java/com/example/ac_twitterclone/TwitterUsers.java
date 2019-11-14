package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;
    private String followedUsers = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        setTitle("Users");

        listView = findViewById(R.id.xlistViewUsers);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try {
            final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null) {
                        if (users.size() > 0) {
                            for (ParseUser user : users) {
                                tUsers.add(user.getUsername());
                                FancyToast.makeText(TwitterUsers.this, "user size is " + users.size(),
                                        FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                            } // end of for(Parse...)
                            listView.setAdapter(adapter);
                            for (String twitterUser : tUsers){
                                if (ParseUser.getCurrentUser().getList("followList") != null){
                                    if (ParseUser.getCurrentUser().getList("followList").contains(twitterUser)){
                                        followedUsers = followedUsers + twitterUser+ "\n";
                                        listView.setItemChecked(tUsers.indexOf(twitterUser),true);
                                        FancyToast.makeText(TwitterUsers.this, "You are following: " +"\n"+ followedUsers,
                                                FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                                    }
                                }
                            } // end of for(String...)
                        }   //end of if(users...)
                    }   // end of if(e==null)
                }   // end of done
            }); // end of findInBackground
        } catch (Exception e){
            FancyToast.makeText(TwitterUsers.this, "no User error " + e,
                    FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        }

        FancyToast.makeText(this,"Welcome " + ParseUser.getCurrentUser().getUsername(),
                FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

    }   // End of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }   // End of onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.xitmLogout:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(TwitterUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case R.id.xitmTweet:
                Intent intent = new Intent(this, SendTweetActivity.class );
                startActivity(intent);
                break;

        }   //end of switch

        return super.onOptionsItemSelected(item);
    }   // End of onOptionsItemSelected

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            FancyToast.makeText(this,tUsers.get(position) + " is now followed",
                    FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().add("followList",tUsers.get(position));

        } else {
            FancyToast.makeText(this,tUsers.get(position) + " is now not followed",
                    FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().getList("followList").remove(tUsers.get(position));
            List  currentFollowList = ParseUser.getCurrentUser().getList("followList");
            ParseUser.getCurrentUser().remove("followList");
            ParseUser.getCurrentUser().put("followList",currentFollowList);
        }   // end if if/else

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    FancyToast.makeText(TwitterUsers.this,"Saved",
                            FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                }
            }
        });

    }// of onItemClick
}
