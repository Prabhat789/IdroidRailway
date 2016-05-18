package com.pktworld.railway.parseutils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pktworld.railway.R;
import com.pktworld.railway.util.ApplicationConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ubuntu1 on 15/3/16.
 */
public class OneToOneChatActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "userId";
    private static final String USER_NAME_KEY = "userName";
    public static int isAppAlive = 0;
    private static String sUserId, sUserName,groupName;
    private static ArrayList<Message> mMessages;
    private static OneToOneChatListAdapter mAdapter;
    private static RecyclerView mRecyclerView;
    private static String friendObjectId = "";
    String userNmae = "";
    private EditText etMessage;
    private Button btSend;
    private RecyclerView.LayoutManager mLayoutManager;
    private int iconId = 0;
    public static Set<String> myStrings;

    public static void receiveMessage() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        //query.fromLocalDatastore();
        query.whereContains("groupId", friendObjectId);
        query.orderByDescending("createdAt");
        //query.setLimit(50);
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    Collections.reverse(messages);
                    mMessages.addAll(messages);
                    myStrings = new HashSet<String>();
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();
                    mRecyclerView.scrollToPosition(mMessages.size() - 1);
                    try{
                        for (int i=0;i<= messages.size();i++){
                            myStrings.add(messages.get(i).getUserId());
                        }
                    }catch (IndexOutOfBoundsException e1){
                        e1.printStackTrace();
                    }

                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_chat);
        friendObjectId = getIntent().getStringExtra(ApplicationConstants.FLAG1);
        userNmae = getIntent().getStringExtra(ApplicationConstants.FLAG2);
        groupName = getIntent().getStringExtra(ApplicationConstants.FLAG3);
        refreshMessages();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        isAppAlive = 1;
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser();

        } else {
            // login();
        }
    }

    @Override
    protected void onResume() {
        isAppAlive = 1;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isAppAlive = 0;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isAppAlive = 0;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        isAppAlive = 1;
        super.onStart();
    }

    @Override
    protected void onStop() {
        isAppAlive = 0;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void refreshMessages() {
        receiveMessage();
    }

    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        sUserName = ParseUser.getCurrentUser().getString("name");
        setupMessagePosting();
    }

    @SuppressLint("SimpleDateFormat")
    private String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a dd-MM-yy");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
        // TODO Auto-generated method stub

    }

    private void setupMessagePosting() {

        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        mRecyclerView = (RecyclerView)findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMessages = new ArrayList<Message>();
        mAdapter = new OneToOneChatListAdapter(OneToOneChatActivity.this, sUserId, mMessages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                //call smooth scroll
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Message message = new Message();
                message.setUserId(sUserId);
                message.setUserName(sUserName);
                message.setGroupId(friendObjectId);
                message.setDateTime(getDateTime());
                message.setProfileImage((String) ParseUser.getCurrentUser().get("profileImage"));
                message.setBody(body);
                //message.setfriendobjectId(ParseUser.getCurrentUser().getObjectId() + friendObjectId);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            receiveMessage();
                            ParseSetUp.sendPush(groupName + ": " + body, myStrings);
                        }

                    }
                });
                etMessage.setText("");
            }
        });
    }


}
