package com.idroidms.railway.parseutils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idroidms.railway.R;
import com.idroidms.railway.util.AppUtils;
import com.idroidms.railway.util.ApplicationConstants;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
public class OneToOneChatActivity extends AppCompatActivity implements ImageChooserListener {
    private static final String USER_ID_KEY = "userId";
    private static final String USER_NAME_KEY = "userName";
    private final static String TAG = "ICA";
    private ImageChooserManager imageChooserManager;
    private String filePath;
    private int chooserType;
    private boolean isActivityResultOver = false;
    private String originalFilePath;
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
    private int iconId = 1;
    public static Set<String> myStrings;
    private Dialog dialog;
    private ImageView imageCamera, imageGallery;
    //private boolean imageChoosen = false;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        friendObjectId = getIntent().getStringExtra(ApplicationConstants.FLAG1);
        userNmae = getIntent().getStringExtra(ApplicationConstants.FLAG2);
        groupName = getIntent().getStringExtra(ApplicationConstants.FLAG3);
        getSupportActionBar().setTitle(userNmae);
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

        //checkForSharedImage(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, iconId, 0, "").setIcon(R.drawable.ic_attach)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }else if (id == iconId){
            imagePickerDialog(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                postMessage(body);
            }
        });
    }

    void postMessage(final String body){
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
                    JSONObject object = new JSONObject();
                    try{
                        object.accumulate("objectId",friendObjectId);
                        object.accumulate("groupName",userNmae);
                        object.accumulate("message", groupName + ": " + body);
                        ParseSetUp.sendPush(object.toString(), myStrings);
                    }catch (JSONException e1){
                        e1.printStackTrace();
                        ParseSetUp.sendPush(groupName + ": " + body, myStrings);
                    }

                }

            }
        });
        etMessage.setText("");
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
           // pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            //pbar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");
                    //loadImage(imageViewThumbnail, image.getFilePathOriginal());
                    //imageChoosen  = true;
                   // postMessage();
                    processUploadImage(originalFilePath);
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });
    }


    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                Toast.makeText(OneToOneChatActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "Saving Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        outState.putBoolean("activity_result_over", isActivityResultOver);
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        outState.putString("orig", originalFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("activity_result_over")) {
                isActivityResultOver = savedInstanceState.getBoolean("activity_result_over");
                originalFilePath = savedInstanceState.getString("orig");

            }
        }
        Log.i(TAG, "Restoring Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        Log.i(TAG, "Activity Result Over: " + isActivityResultOver);
        if (isActivityResultOver) {
            populateData();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void populateData() {
        Log.i(TAG, "Populating Data");
    }


    /*private void checkForSharedImage(Intent intent) {
        if (intent != null) {
            if (intent.getAction() != null && intent.getType() != null && intent.getExtras() != null) {
                ImageChooserManager m = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE);
                m.setImageChooserListener(this);

                m.submit(ChooserType.REQUEST_PICK_PICTURE, IntentUtils.getIntentForMultipleSelection(intent));
            }
        }
    }*/

    void imagePickerDialog(Context mContext) {

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_image_picker);
        dialog.setCancelable(true);
        imageCamera = (ImageView) dialog.findViewById(R.id.imageCamera);
        imageGallery = (ImageView) dialog.findViewById(R.id.imageGallery);
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                takePicture();
            }
        });
        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chooseImage();
            }
        });

        dialog.show();
    }

   /* private void uploadImage(ParseFile file) {
        // TODO Auto-generated method stub
        try {
            final ParseObject imgupload = new ParseObject("ImageTable");
            imgupload.put("ImageFile", file);
            imgupload.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e == null) {

                    } else {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            //stopSelf();
        }
    }*/

    private void processUploadImage(final String imagePath) {
        // TODO Auto-generated method stub
        try {
            Bitmap imageBitmap = null;
            byte[] ImageArray = null;
            //final String userName = ParseUser.getCurrentUser().getEmail();

            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                imageBitmap = BitmapFactory.decodeFile(imgFile
                        .getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50,
                        stream);
                ImageArray = stream.toByteArray();
                final ParseFile file = new ParseFile(AppUtils.imageUploadTimeStamp()  + ".JPEG", ImageArray);
                file.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException arg0) {
                        // TODO Auto-generated method stub
                        if (arg0 == null) {
                            postMessage(file.getUrl());
                            //uploadImage(file.getUrl());
                        } else {
                            arg0.printStackTrace();
                            //stopSelf();

                        }
                    }
                });

            } else {

            }

        } catch (Exception e) {
            // TODO: handle exception
            //stopSelf();
        }

    }
}
